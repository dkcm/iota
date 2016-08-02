/**
 * Crossover.java v0.3  24 November 2014 11:11:43 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.Signal.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crossover strategy.
 * <p>
 * Buys when the fast time series crosses over the slow time series from below<br>
 * Sells when the fast time series crosses under the slow time series from above<br>
 * <p>
 * Supports 1-value lookback.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class Crossover extends AbstractStrategy {

  private final int           fastIndex;
  private final int           slowIndex;

  private static final Logger logger = LoggerFactory.getLogger(Crossover.class);

  public Crossover(final Indicator indicator) {
    this(indicator, ZERO, ONE);
  }

  public Crossover(final Indicator indicator, final int fastIndex, final int slowIndex) {
    super(indicator);
    if (fastIndex < ZERO || slowIndex < ZERO || fastIndex == slowIndex) {
      throw new IllegalArgumentException();
    }

    this.fastIndex = fastIndex;
    this.slowIndex = slowIndex;
  }

  public Crossover(final Indicator fast, final Indicator slow) {
    super(fast, slow);

    fastIndex = ZERO;
    slowIndex = ONE;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final String ohlcvName = ohlcv.toString();

    // indicator fast and slow components
    final TimeSeries fast;
    final TimeSeries slow;
    if (indicators.size() > ONE) {
      fast = indicatorValues.get(fastIndex).get(ZERO);
      slow = indicatorValues.get(slowIndex).get(ZERO);
    }
    else {
      final List<TimeSeries> indicatorComponents = indicatorValues.get(ZERO);
      fast = indicatorComponents.get(fastIndex);
      slow = indicatorComponents.get(slowIndex);
    }

    final int size = fast.size();

    // initialise
    int today;
    final SignalTimeSeries signals;
    if (lookback >= size) {
      today = ZERO;
      signals = new SignalTimeSeries(toString(), size - ONE);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
      today = (size - ONE - lookback);
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }
    double fastYesterday = fast.value(today);
    double slowYesterday = slow.value(today);

    // generate signals
    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
         ++today < size;
         ++s, ++c) {
      final double fastToday = fast.value(today);
      final double slowToday = slow.value(today);

      final String date = fast.date(today);
      final double close = ohlcv.close(c);

      final Signal signal;
      // fast indicator crosses over the slow indicator from below
      if (buy(fastYesterday, slowYesterday, fastToday, slowToday)) {
        signal = BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      // fast indicator crosses under the slow indicator from above
      else if (sell(fastYesterday, slowYesterday, fastToday, slowToday)) {
        signal = SELL;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else {
        signal = NONE;
        logger.debug(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      signals.set(date, signal, s);

      // shift forward
      fastYesterday = fastToday;
      slowYesterday = slowToday;
    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    return crossover(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

  @Override
  protected boolean sell(final double... doubles) {
    return crossunder(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

  @Override
  public String toString() {
    String strategyName;
    if (indicators.size() > ONE) {
      strategyName = indicators.get(fastIndex) + " x " + indicators.get(slowIndex) + SPACE;
    }
    else {
      strategyName = indicators.get(ZERO) + SPACE;
    }
    return strategyName + super.toString();
  }

}
