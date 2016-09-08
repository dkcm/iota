/**
 * CompositeStrategy.java  v0.3  2 August 2016 6:36:44 pm
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
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class CompositeStrategy implements Strategy {
  // TODO v0.4 modes: AND, OR, XOR; search forward- or backward-only

  private final int            window;
  private final List<Strategy> strategies;
  private final String         name      = getClass().getSimpleName();

  private static final int     ZERO      = 0;
  private static final int     ONE       = 1;
  private static final int     NOT_FOUND = -1;

  private static final Logger  logger    = LoggerFactory.getLogger(CompositeStrategy.class);

  public CompositeStrategy(final int window, final Strategy... strategies) {
    if (window < ZERO) {
      throw new IllegalArgumentException("Negative window of interest");
    }
    if (strategies.length <= ONE) {
      throw new IllegalArgumentException("Two or more strategies required");
    }

    this.window = window;
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
    // 3. Search next signals series forwards and backwards for buy / sell
    //    within window of interest
    // a) If matching signal found, continue step 3 until last signals series
    // b) Else go to step 2

    // generate signals
    final List<SignalTimeSeries> signals = generateSignals(ohlcv, lookback);

    // aggregate signals across strategies
    final String ohlcvName = ohlcv.toString();
    final SignalTimeSeries reference = signals.remove(ZERO);
    final String[] dates = reference.dates();
    final int length = reference.size();
    final int offset = ohlcv.size() - length;

    final SignalTimeSeries composite = new SignalTimeSeries(name, length);
    for (int today = ZERO; today < length; ++today) {
      final Signal signal = reference.signal(today);
      if (signal == BUY || signal == SELL) {
        // search backwards and forwards in other signals series for matching
        // signals, defaulting to Signal.NONE if none are found

        // search backwards
        final int backwards = (today >= window) ?
                              findMaxMatching(signal, today - window, today, signals) :
                              NOT_FOUND;
        // search forwards
        final int forwards = (today + window < length) ?
                             findMaxMatching(signal, today, today + window, signals) :
                             NOT_FOUND;

        if (backwards > NOT_FOUND) { // matching signal found
          setSignal(signal, today, composite, dates);
          logger.info(TRADE_SIGNAL, signal, ohlcvName, dates[today], ohlcv.close(today + offset));
        }
        else {
          setSignal(NONE, today, composite, dates);
        }

        if (forwards > NOT_FOUND) {  // matching signal found
          // skip forward and backfill
          while (++today < forwards) {
            setSignal(NONE, today, composite, dates);
          }
          setSignal(signal, today, composite, dates);
          logger.info(TRADE_SIGNAL, signal, ohlcvName, dates[today], ohlcv.close(today + offset));
        }
      }
      else {  // no buy / sell signal
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

  private static final int findMaxMatching(final Signal reference,
                                           final int start,
                                           final int end,
                                           final List<SignalTimeSeries> others) {
    int maxMatchingSignalIndex = NOT_FOUND;
    for (final SignalTimeSeries other : others) {
      // find maximum matching index across all signal series
      final int matchingSignalIndex = findMatching(reference, start, end, other);
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

  private static final int findMatching(final Signal reference,
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
