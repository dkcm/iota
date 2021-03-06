/**
 * CompositeStrategy.java  v0.7  2 August 2016 6:36:44 pm
 *
 * Copyright � 2016-2019 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.Signal.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@code Strategy} of strategies. Signals only when unanimous (i.e. all
 * trading strategies are in agreement).
 *
 *
 * @author Daniel Kuan
 * @version 0.7
 */
public class CompositeStrategy implements Strategy {

  private final int            window;
  private final boolean        isSearchForwards;
  private final boolean        isSearchBackwards;
  private final List<Strategy> strategies;
  private final String         name      = getClass().getSimpleName();

  private static final int     ZERO      = 0;
  private static final int     ONE       = 1;
  private static final int     NOT_FOUND = -1;

  private static final Logger  logger    = LoggerFactory.getLogger(CompositeStrategy.class);

  /**
   *
   *
   * @param window forward and / or backward search space
   * @param strategies {@code Strategy} components making up the composite
   */
  public CompositeStrategy(final int window, final Strategy... strategies) {
    this(window, true, true, strategies);
  }

  /**
   *
   *
   * @param window forward and / or backward search space
   * @param searchForwards {@code true} if searching forward in time for
   *          matching signals
   * @param searchBackwards {@code true} if searching backward in time for
   *          matching signals
   * @param strategies {@code Strategy} components making up the composite
   */
  public CompositeStrategy(final int window, final boolean searchForwards, final boolean searchBackwards, final Strategy... strategies) {
    if (window < ONE) {
      throw new IllegalArgumentException("Positive window of interest required");
    }
    if (strategies.length <= ONE) {
      throw new IllegalArgumentException("Two or more strategies required");
    }
    throwExceptionIfNull(strategies);

    this.window = window;
    isSearchForwards = searchForwards;
    isSearchBackwards = searchBackwards;
    this.strategies = Arrays.asList(strategies);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return execute(ohlcv, MAX_LOOKBACK);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    // Algorithm:
    // 1. Pick signals series with fewest buys & sells
    // 2. Iterate through series until next buy / sell
    // 3. Search next signals series forwards and backwards, if enabled, for
    //    buy / sell within window of interest
    // a) If matching signal found, continue step 3 until last signals series
    // b) Else go to step 2

    // generate signals from strategies
    final List<SignalTimeSeries> signals = generateSignals(ohlcv, lookback);

    // aggregate signals across strategies
    return aggregateSignals(signals, ohlcv);
  }

  private List<SignalTimeSeries> generateSignals(final OHLCVTimeSeries ohlcv, final int lookback) {
    final List<SignalTimeSeries> signals = new ArrayList<>(strategies.size());

    int shortestLength = Integer.MAX_VALUE;
    for (final Strategy strategy : strategies) {
      final SignalTimeSeries timeSeries = strategy.execute(ohlcv, lookback);
      signals.add(timeSeries);
      shortestLength = Math.min(timeSeries.size(), shortestLength);
    }

    // truncate to cater to different signal sizes
    truncate(signals, shortestLength);

    return signals;
  }

  private static void truncate(final List<SignalTimeSeries> signals, final int shortestLength) {
    final ListIterator<SignalTimeSeries> iterator = signals.listIterator();
    while (iterator.hasNext()) {
      final SignalTimeSeries series = iterator.next();
      final int size = series.size();
      // truncate
      if (size > shortestLength) {
        final SignalTimeSeries newSeries = new SignalTimeSeries(series.toString(), shortestLength);
        final int offset = size - shortestLength;
        for (int i = offset, j = ZERO; i < size; ++i, ++j) { // copy
          newSeries.set(series.date(i), series.signal(i), j);
        }
        iterator.set(newSeries);
      }
    }
    logger.debug("Signals truncated to length: {}", shortestLength);
  }

  private SignalTimeSeries aggregateSignals(final List<SignalTimeSeries> signals, final OHLCVTimeSeries ohlcv) {
    final SignalTimeSeries references = signals.remove(ZERO);
    final int length = references.size();
    logger.debug("Aggregating signals using {} as reference", references);

    final SignalTimeSeries composite = new SignalTimeSeries(name, length);
    System.arraycopy(references.dates(), ZERO, composite.dates(), ZERO, length);

    for (int today = ZERO; today < length; ++today) {
      final Signal reference = references.signal(today);
      if (reference == BUY || reference == SELL) {
        // search backwards and forwards in other signals series for matching
        // signals, defaulting to Signal.NONE if none are found
        logger.debug("{} found at index {} in {}",
                     reference,
                     today,
                     references);
        today = searchForMatchAndSet(reference, signals, today, composite, ohlcv);
      }
      else { // no buy / sell signal
        setSignal(NONE, today, composite);
      }
    }

    return composite;
  }

  private int searchForMatchAndSet(final Signal reference,
                                   final List<SignalTimeSeries> signals,
                                   final int start,
                                   final SignalTimeSeries composite,
                                   final OHLCVTimeSeries ohlcv) {
    int today = start;
    // search backwards
    final int backwards = searchBackwards(reference, signals, today);
    // search current
    final int current = findMaxMatch(reference, today, today + ONE, signals);

    final int offset = ohlcv.size() - composite.size();
    if (backwards > NOT_FOUND || current > NOT_FOUND) { // matching signal found
      setSignal(reference, today, composite);
      logger.info(TRADE_SIGNAL,
                  reference,
                  ohlcv.toString(),
                  composite.date(today),
                  ohlcv.close(today + offset));
    }
    else {
      setSignal(NONE, today, composite);
    }

    // search forwards
    final int forwards = searchForwards(reference, signals, today, composite);
    if (forwards > NOT_FOUND) {  // matching signal found
      // skip forward and backfill, masking other signals (TODO bug?)
      while (++today < forwards) {
        setSignal(NONE, today, composite);
      }
      setSignal(reference, forwards, composite);
      logger.info(TRADE_SIGNAL,
                  reference,
                  ohlcv.toString(),
                  composite.date(forwards),
                  ohlcv.close(forwards + offset));
    }

    // combine backwards and forwards search results
    if (backwards == NOT_FOUND && current == NOT_FOUND && forwards == NOT_FOUND) {
      final int match = searchBackwardsAndForwards(reference, signals, today, composite);
      if (match > NOT_FOUND) {
        today = match;
        setSignal(reference, today, composite);
        logger.info(TRADE_SIGNAL,
                    reference,
                    ohlcv.toString(),
                    composite.date(today),
                    ohlcv.close(today + offset));
      }
    }

    return today;
  }

  private int searchBackwards(final Signal reference,
                              final List<SignalTimeSeries> others,
                              final int today) {
    return isSearchBackwards(today, window) ?
           findMaxMatch(reference, today - window, today, others) :
           NOT_FOUND;
  }

  private boolean isSearchBackwards(final int today, final int offset) {
    return isSearchBackwards && (today >= offset);
  }

  private int searchForwards(final Signal reference,
                             final List<SignalTimeSeries> others,
                             final int today,
                             final SignalTimeSeries composite) {
    return isSearchForwards(today, window, composite.size()) ?
           findMaxMatch(reference, today + ONE, today + window + ONE, others) :
           NOT_FOUND;
  }

  private boolean isSearchForwards(final int today, final int offset, final int size) {
    return isSearchForwards && (today + offset < size);
  }

  private int searchBackwardsAndForwards(final Signal reference,
                                         final List<SignalTimeSeries> others,
                                         final int today,
                                         final SignalTimeSeries composite) {
    final int halfWindow = window >> ONE;
    return (isSearchBackwards(today, halfWindow) &&
            isSearchForwards(today, halfWindow, composite.size())) ?
           findMaxMatch(reference, today - halfWindow, today + halfWindow + ONE, others) :
           NOT_FOUND;
  }

  private static int findMaxMatch(final Signal reference,
                                  final int start,
                                  final int end,
                                  final List<SignalTimeSeries> others) {
    int maxMatchingSignalIndex = NOT_FOUND;
    // find maximum matching index across all signal series
    for (final SignalTimeSeries other : others) {
      final int matchingSignalIndex = findMatch(reference, start, end, other);
      if (matchingSignalIndex > NOT_FOUND) {
        maxMatchingSignalIndex = Math.max(maxMatchingSignalIndex, matchingSignalIndex);
      }
      else {
        maxMatchingSignalIndex = NOT_FOUND;
        logger.debug("No matching {} found in {}", reference, other);
        break;
      }
    }
    return maxMatchingSignalIndex;
  }

  private static int findMatch(final Signal reference,
                               final int start,
                               final int end,
                               final SignalTimeSeries signals) {
    int matchingSignalIndex = NOT_FOUND;
    // find first match in a signal series
    for (int i = start; i < end; ++i) {
      if (reference == signals.signal(i)) {
        matchingSignalIndex = i;
        logger.debug("Matching {} found at index {} in {}",
                     reference,
                     matchingSignalIndex,
                     signals);
        break;
      }
    }
    return matchingSignalIndex;
  }

  private static void setSignal(final Signal signal,
                                final int index,
                                final SignalTimeSeries signals) {
    signals.signal(signal, index);
  }

  @Override
  public String toString() {
    return name;
  }

  private static void throwExceptionIfNull(final Strategy... strategies) {
    for (int i = 0; i < strategies.length; ++i) {
      final Strategy strategy = strategies[i];
      if (strategy == null) {
        throw new NullPointerException("Strategy " + i + " is null");
      }
    }
  }

  @Override
  public int compareTo(final Strategy o) {
    return name.compareTo(o.toString());
  }

}
