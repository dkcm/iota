/**
 * Crossover.java  v0.5  24 November 2014 11:11:43 pm
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
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
 *
 * <p>Buys when the fast time series crosses over the slow time series from below<br>
 * Sells when the fast time series crosses under the slow time series from above<br>
 *
 * <p>Supports 1-value lookback.
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class Crossover extends AbstractStrategy {

  private final int           fastBuyIndex;
  private final int           slowBuyIndex;
  private final int           fastSellIndex;
  private final int           slowSellIndex;

  private static final Logger logger = LoggerFactory.getLogger(Crossover.class);

  public Crossover(final Indicator indicator) {
    this(indicator, ZERO, ONE);
  }

  public Crossover(final Indicator indicator, final int fastIndex, final int slowIndex) {
    this(indicator, fastIndex, slowIndex, fastIndex, slowIndex);
  }

  public Crossover(final Indicator indicator, final int fastBuyIndex, final int slowBuyIndex, final int fastSellIndex, final int slowSellIndex) {
    super(indicator);
    throwExceptionIfNegative(fastBuyIndex, slowBuyIndex, fastSellIndex, slowSellIndex);
    if (fastBuyIndex == slowBuyIndex || fastSellIndex == slowSellIndex) {
      throw new IllegalArgumentException();
    }

    this.fastBuyIndex = fastBuyIndex;
    this.slowBuyIndex = slowBuyIndex;
    this.fastSellIndex = fastSellIndex;
    this.slowSellIndex = slowSellIndex;
  }

  public Crossover(final Indicator fast, final Indicator slow) {
    this(fast, slow, ZERO, ZERO);
  }

  public Crossover(final Indicator fast, final Indicator slow, final int fastIndex, final int slowIndex) {
    this(fast, slow, fastIndex, slowIndex, fastIndex, slowIndex);
  }

  public Crossover(final Indicator fast, final Indicator slow, final int fastBuyIndex, final int slowBuyIndex, final int fastSellIndex, final int slowSellIndex) {
    super(fast, slow);
    throwExceptionIfNegative(fastBuyIndex, slowBuyIndex, fastSellIndex, slowSellIndex);

    this.fastBuyIndex = fastBuyIndex;
    this.slowBuyIndex = slowBuyIndex;
    this.fastSellIndex = fastSellIndex;
    this.slowSellIndex = slowSellIndex;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final String ohlcvName = ohlcv.toString();

    // indicator fast and slow components
    final TimeSeries fastBuy;
    final TimeSeries slowBuy;
    final TimeSeries fastSell;
    final TimeSeries slowSell;
    if (indicators.size() > ONE) {
      final List<TimeSeries> fasts = indicatorValues.get(ZERO);
      fastBuy = fasts.get(fastBuyIndex);
      fastSell = fasts.get(fastSellIndex);
      final List<TimeSeries> slows = indicatorValues.get(ONE);
      slowBuy = slows.get(slowBuyIndex);
      slowSell = slows.get(slowSellIndex);
    }
    else {
      final List<TimeSeries> indicatorComponents = indicatorValues.get(ZERO);
      fastBuy = fastSell = indicatorComponents.get(fastBuyIndex);
      slowBuy = slowSell = indicatorComponents.get(slowBuyIndex);
    }

    final int size = fastBuy.size();

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
    double fastBuyYesterday = fastBuy.value(today);
    double slowBuyYesterday = slowBuy.value(today);
    double fastSellYesterday = fastSell.value(today);
    double slowSellYesterday = slowSell.value(today);

    // generate signals
    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
         ++today < size;
         ++s, ++c) {
      final double fastBuyToday = fastBuy.value(today);
      final double slowBuyToday = slowBuy.value(today);
      final double fastSellToday = fastSell.value(today);
      final double slowSellToday = slowSell.value(today);

      final String date = fastBuy.date(today);
      final double close = ohlcv.close(c);

      final Signal signal;
      // fast indicator crosses over the slow indicator from below
      if (buy(fastBuyYesterday, slowBuyYesterday, fastBuyToday, slowBuyToday)) {
        signal = BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      // fast indicator crosses under the slow indicator from above
      else if (sell(fastSellYesterday, slowSellYesterday, fastSellToday, slowSellToday)) {
        signal = SELL;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else {
        signal = NONE;
        logger.debug(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      signals.set(date, signal, s);

      // shift forward
      fastBuyYesterday = fastBuyToday;
      slowBuyYesterday = slowBuyToday;
      fastSellYesterday = fastSellToday;
      slowSellYesterday = slowSellToday;
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
      strategyName = indicators.get(ZERO) + " x " + indicators.get(ONE) + SPACE;
    }
    else {
      strategyName = indicators.get(ZERO) + SPACE;
    }
    return strategyName + super.toString();
  }

}
