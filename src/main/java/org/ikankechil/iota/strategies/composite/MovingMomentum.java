/**
 * MovingMomentum.java  v0.1  3 December 2015 7:04:07 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.composite;

import static org.ikankechil.iota.Signal.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.momentum.Stochastic;
import org.ikankechil.iota.indicators.trend.MACD;
import org.ikankechil.iota.indicators.trend.SMA;
import org.ikankechil.iota.strategies.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Moving Momentum
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:moving_momentum<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class MovingMomentum extends AbstractStrategy {

  private static final int    BOUNCE   = 80;
  private static final int    PULLBACK = 20;

  private static final Logger logger   = LoggerFactory.getLogger(MovingMomentum.class);

  public MovingMomentum() {
    super(new SMA(20), new SMA(150), new Stochastic(), new MACD());
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    // Buy:
    // 1. Moving averages show a bullish trading bias with the 20-day SMA
    //    trading above the 150-day SMA
    // 2. Stochastic Oscillator moves below 20 to signal a pullback
    // 3. MACD-Histogram moves into positive territory to signal an upturn after
    //    the pullback
    //
    // Sell:
    // 1. Moving averages show a bearish trading bias with the 20-day SMA
    //    trading below the 150-day SMA
    // 2. Stochastic Oscillator moves above 80 to signal a bounce
    // 3. MACD-Histogram moves into negative territory to signal a downturn
    //    after the bounce
    final TimeSeries shortSMA = indicatorValues.get(ZERO).get(ZERO);
    final TimeSeries longSMA = indicatorValues.get(ONE).get(ZERO);
    final TimeSeries stochastic = indicatorValues.get(TWO).get(ZERO);
    final TimeSeries macdHistogram = indicatorValues.get(THREE).get(THREE);

    // generate signals
    final SignalTimeSeries signals = null;
    final String ohlcvName = ohlcv.toString();
    final int size = shortSMA.size();
    for (int today = ONE, yesterday = ZERO, c = today + ohlcv.size() - size;
         today < size;
         ++today, ++yesterday, ++c) {
      final String date = ohlcv.date(c);
      final double close = ohlcv.close(c);

      Signal signal;
      // long
      if (buy(shortSMA.value(today),
              longSMA.value(today),
              stochastic.value(yesterday),
              stochastic.value(today))) {
        if (crossover(macdHistogram.value(yesterday), macdHistogram.value(today), ZERO)) { // TODO make it after pullback
          signal = BUY;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }
      // short
      else if (sell(shortSMA.value(today),
                    longSMA.value(today),
                    stochastic.value(yesterday),
                    stochastic.value(today))) {
        if (crossunder(macdHistogram.value(yesterday), macdHistogram.value(today), ZERO)) {
          signal = SELL;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }

    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    final double shortSMA = doubles[ZERO];
    final double longSMA = doubles[ONE];
    final double stochasticYesterday = doubles[TWO];
    final double stochasticToday = doubles[THREE];

    return ((shortSMA > longSMA) &&
            crossunder(stochasticYesterday, stochasticToday, PULLBACK));
  }

  @Override
  protected boolean sell(final double... doubles) {
    final double shortSMA = doubles[ZERO];
    final double longSMA = doubles[ONE];
    final double stochasticYesterday = doubles[TWO];
    final double stochasticToday = doubles[THREE];

    return ((shortSMA < longSMA) &&
            crossover(stochasticYesterday, stochasticToday, BOUNCE));
  }

}
