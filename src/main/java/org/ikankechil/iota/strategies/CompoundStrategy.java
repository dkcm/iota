/**
 * CompoundStrategy.java  v0.1  2 August 2016 6:36:44 pm
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
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CompoundStrategy implements Strategy {

  private final int            window;
  private final List<Strategy> strategies;

  private static final int     ZERO      = 0;
  private static final int     ONE       = 1;
  private static final int     NOT_FOUND = -1;

  private static final Logger  logger    = LoggerFactory.getLogger(CompoundStrategy.class);

  public CompoundStrategy(final int window, final Strategy... strategies) {
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
    final String ohlcvName = ohlcv.toString();

    // generate signals
    final List<SignalTimeSeries> signals = new ArrayList<>(strategies.size());
    int shortestLength = Integer.MAX_VALUE;
    for (final Strategy strategy : strategies) {
      final SignalTimeSeries timeSeries = strategy.execute(ohlcv, lookback);
      signals.add(timeSeries);
      shortestLength = Math.min(timeSeries.size(), shortestLength);
    }

    // truncate to cater to different signal sizes
    truncate(signals, shortestLength);

    // aggregate signals across strategies
    final SignalTimeSeries compoundSignals = new SignalTimeSeries(toString(), shortestLength);
    final SignalTimeSeries signals1 = signals.remove(ZERO);
    for (int today = ZERO, c = ohlcv.size() - shortestLength;
         today < shortestLength;
         ++today, ++c) {
      final double close = ohlcv.close(c);

      final Signal signal = signals1.signal(today);
      if (signal == BUY || signal == SELL) {
        // search backwards
        if (today > window) {
          final int backwards = findMaxMatching(signal, today - window, today, signals);
          if (backwards > NOT_FOUND) {
            final String date = signals1.date(today);
            compoundSignals.set(date, signal, today);
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }

        // search forwards
        if (today + window < shortestLength) {
          final int forwards = findMaxMatching(signal, today, today + window, signals);
          if (forwards > NOT_FOUND) {
            today = forwards; // skip forwards
            // TODO need to backfill skipped indices with NONE
            final String date = signals1.date(today);
            compoundSignals.set(date, signal, today);
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }
      }
      else {  // no buy / sell signal
        // TODO possible to skip forward?
        final String date = signals1.date(today);
        compoundSignals.set(date, NONE, today);
        logger.debug(TRADE_SIGNAL, NONE, ohlcvName, date, close);
      }

      // Algorithm:
      // 1. Pick signals series with fewest buys & sells
      // 2. Iterate through series until next buy / sell
      // 3. Search next signals series forwards and backwards for buy / sell
      //    within window of interest
      // a) If matching signal found, continue step 3 until last signals series
      // b) Else go to step 2
    }

    return compoundSignals;
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
  }

  private static final int findMaxMatching(final Signal reference,
                                           final int start,
                                           final int end,
                                           final List<SignalTimeSeries> others) {
    int maxMatchingSignalIndex = NOT_FOUND;
    for (final SignalTimeSeries other : others) {
      // find maximum matching index across all signal series
      maxMatchingSignalIndex = Math.max(maxMatchingSignalIndex,
                                        findMatching(reference,
                                                     start,
                                                     end,
                                                     other));
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
        break;
      }
    }
    return matchingSignalIndex;
  }

}
