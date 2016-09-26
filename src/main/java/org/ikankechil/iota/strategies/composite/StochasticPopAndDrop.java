/**
 * StochasticPopAndDrop.java  v0.1  8 December 2014 9:09:15 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.momentum.Stochastic;
import org.ikankechil.iota.indicators.trend.ADX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stochastic Pop and Drop by Jake Bernstein and David Steckler
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:stochastic_pop_drop
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V18/C08/071POP.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class StochasticPopAndDrop extends AbstractStrategy {

  // thresholds
  private static final int    BIAS       = 50;
  private static final int    WEAK_TREND = 20;
  private static final int    POP        = 80;
  private static final int    DROP       = 20;

  private static final Logger logger     = LoggerFactory.getLogger(StochasticPopAndDrop.class);

  public StochasticPopAndDrop() {
    super(new Stochastic(70, 3, 3),
          new ADX(),
          new Stochastic(14, 3, 3));
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    // Buy:
    // 1. 70-day Stochastic Oscillator is above 50
    // 2. 14-day Average Directional Index (ADX) is below 20
    // 3. 14-day Stochastic Oscillator surges above 80
    // 4. Stock breaks out on high volume and/or breaks consolidation support
    //
    // Sell:
    // 1. 70-day Stochastic Oscillator is below 50
    // 2. 14-day Average Directional Index (ADX) is below 20
    // 3. 14-day Stochastic Oscillator plunges below 20
    // 4. Stock declines on high volume and/or breaks consolidation support
    //
    final double[] longTermStochastic = indicatorValues.get(ZERO).get(ZERO).values();
    final double[] adx = indicatorValues.get(ONE).get(ZERO).values();
    final double[] shortTermStochastic = indicatorValues.get(TWO).get(ZERO).values();

    final int size = longTermStochastic.length;

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

    double shortTermStochasticYesterday = shortTermStochastic[today];

    // generate signals
    final String ohlcvName = ohlcv.toString();
    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
         ++today < size;
         ++s, ++c) {
      final String date = ohlcv.date(c);
      final double close = ohlcv.close(c);

      Signal signal = Signal.NONE;
      final double shortTermStochasticToday = shortTermStochastic[today];
      // long
      if (longTermStochastic[today] > BIAS) {
        if (adx[today] < WEAK_TREND) {
          if (crossover(shortTermStochasticYesterday, shortTermStochasticToday, POP)) {
            signal = Signal.BUY;
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }
      }
      // short
      else if (longTermStochastic[today] < BIAS) {
        if (adx[today] < WEAK_TREND) {
          if (crossover(shortTermStochasticToday, shortTermStochasticYesterday, DROP)) {
            signal = Signal.SELL;
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }
      }
      signals.set(date, signal, s);

      shortTermStochasticYesterday = shortTermStochasticToday;
    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected boolean sell(final double... doubles) {
    // TODO Auto-generated method stub
    return false;
  }

}
