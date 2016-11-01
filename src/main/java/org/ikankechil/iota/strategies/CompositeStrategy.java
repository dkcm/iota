/**
 * CompositeStrategy.java  v0.5  2 August 2016 6:36:44 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
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
 * A <code>Strategy</code> of strategies.
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class CompositeStrategy implements Strategy {

  private final int            window;
  private final boolean        isSearchForwards;
  private final boolean        isSearchBackwards;
  private final boolean        isUnanimous;
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
   * @param strategies <code>Strategy</code> components making up the composite
   */
  public CompositeStrategy(final int window, final Strategy... strategies) {
    this(window, true, strategies);
  }

  /**
   *
   *
   * @param window forward and / or backward search space
   * @param unanimous <code>true</code> if all trading strategies are in
   *          agreement
   * @param strategies <code>Strategy</code> components making up the composite
   */
  public CompositeStrategy(final int window, final boolean unanimous, final Strategy... strategies) {
    this(window, unanimous, true, true, strategies);
  }

  /**
   *
   *
   * @param window forward and / or backward search space
   * @param unanimous <code>true</code> if all trading strategies are in
   *          agreement
   * @param searchForwards <code>true</code> if searching forward in time for
   *          matching signals
   * @param searchBackwards <code>true</code> if searching backward in time for
   *          matching signals
   * @param strategies <code>Strategy</code> components making up the composite
   */
  public CompositeStrategy(final int window, final boolean unanimous, final boolean searchForwards, final boolean searchBackwards, final Strategy... strategies) {
    if (window < ONE) {
      throw new IllegalArgumentException("Positive window of interest required");
    }
    if (strategies.length <= ONE) {
      throw new IllegalArgumentException("Two or more strategies required");
    }

    this.window = window;
    isSearchForwards = searchForwards;
    isSearchBackwards = searchBackwards;
    isUnanimous = unanimous;
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

    // generate signals
    final List<SignalTimeSeries> signals = generateSignals(ohlcv, lookback);

    // aggregate signals across strategies
    final String ohlcvName = ohlcv.toString();
    final SignalTimeSeries references = signals.remove(ZERO);
    final String[] dates = references.dates();
    final int length = references.size();
    final int offset = ohlcv.size() - length;

    final SignalTimeSeries composite = new SignalTimeSeries(name, length);
    for (int today = ZERO; today < length; ++today) {
      final Signal reference = references.signal(today);
      if (reference == BUY || reference == SELL) {
        // search backwards and forwards in other signals series for matching
        // signals, defaulting to Signal.NONE if none are found

        // search backwards
        final int backwards = isSearchBackwards && (today >= window) ?
                              findMaxMatch(reference, today - window, today, signals) :
                              NOT_FOUND;
        // search current
        final int current = findMaxMatch(reference, today, today + ONE, signals);

        if (backwards > NOT_FOUND || current > NOT_FOUND) { // matching signal found
          setSignal(reference, today, composite, dates);
          logger.info(TRADE_SIGNAL, reference, ohlcvName, dates[today], ohlcv.close(today + offset));
        }
        else {
          setSignal(NONE, today, composite, dates);
        }

        // search forwards
        final int forwards = isSearchForwards && (today + window < length) ?
                             findMaxMatch(reference, today + ONE, today + window + ONE, signals) :
                             NOT_FOUND;
        if (forwards > NOT_FOUND) {  // matching signal found
          // skip forward and backfill
          while (++today < forwards) {
            setSignal(NONE, today, composite, dates);
          }
          setSignal(reference, forwards, composite, dates);
          logger.info(TRADE_SIGNAL, reference, ohlcvName, dates[today], ohlcv.close(today + offset));
        }
      }
      else {  // no buy / sell signal // TODO bug: not unanimous will not check other signal series
        setSignal(NONE, today, composite, dates);
      }
    }

    return composite;
  }

  private final List<SignalTimeSeries> generateSignals(final OHLCVTimeSeries ohlcv, final int lookback) {
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

  private static final void truncate(final List<SignalTimeSeries> signals, final int shortestLength) {
    final ListIterator<SignalTimeSeries> iterator = signals.listIterator();
    while (iterator.hasNext()) {
      final SignalTimeSeries series = iterator.next();
      final int size = series.size();
      // truncate
      if (size > shortestLength) {
        final SignalTimeSeries newSeries = new SignalTimeSeries(series.toString(), shortestLength);
        final int offset = size - shortestLength;
        for (int i = offset, j = ZERO; i < size; ++i, ++j) {
          newSeries.set(series.date(i), series.signal(i), j);
        }
        iterator.set(newSeries);
      }
    }
    logger.debug("Signals truncated to length: {}", shortestLength);
  }

  private static final int findMinMatch(final Signal reference,
                                        final int start,
                                        final int end,
                                        final List<SignalTimeSeries> others) {
    int minMatchingSignalIndex = NOT_FOUND;
    for (final SignalTimeSeries other : others) {
      // find minimum matching index across all signal series
      final int matchingSignalIndex = findMatch(reference, start, end, other);
      if (matchingSignalIndex > NOT_FOUND) {
        minMatchingSignalIndex = matchingSignalIndex;
        break;
      }
      logger.debug("No matching {} in {}", reference, other);
    }
    return minMatchingSignalIndex;
  }

  private final int findMaxMatch(final Signal reference,
                                 final int start,
                                 final int end,
                                 final List<SignalTimeSeries> others) {
    int maxMatchingSignalIndex = NOT_FOUND;
    for (final SignalTimeSeries other : others) {
      // find maximum matching index across all signal series
      final int matchingSignalIndex = findMatch(reference, start, end, other);
      if (matchingSignalIndex > NOT_FOUND) {
        maxMatchingSignalIndex = Math.max(maxMatchingSignalIndex, matchingSignalIndex);
      }
      else if (isUnanimous) {
        maxMatchingSignalIndex = NOT_FOUND;
        logger.debug("No unanimous {} since {}", reference, other);
        break;
      }
      else {
        logger.debug("No matching {} in {}", reference, other);
      }
    }
    return maxMatchingSignalIndex;
  }

  private static final int findMatch(final Signal reference,
                                     final int start,
                                     final int end,
                                     final SignalTimeSeries signals) {
    int matchingSignalIndex = NOT_FOUND;
    for (int i = start; i < end; ++i) {
      if (reference == signals.signal(i)) {
        matchingSignalIndex = i;
        logger.debug("Matching {} found at index {} in {}", reference, matchingSignalIndex, signals);
        break;
      }
    }
    return matchingSignalIndex;
  }

  private static final void setSignal(final Signal signal,
                                      final int index,
                                      final SignalTimeSeries signals,
                                      final String[] dates) {
    final String date = dates[index];
    signals.set(date, signal, index);
  }

  @Override
  public String toString() {
    return name;
  }

}
