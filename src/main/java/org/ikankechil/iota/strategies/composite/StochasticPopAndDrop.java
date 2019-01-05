/**
 * StochasticPopAndDrop.java  v0.2  8 December 2014 9:09:15 PM
 *
 * Copyright © 2014-2019 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.composite;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.momentum.Stochastic;
import org.ikankechil.iota.indicators.trend.ADX;
import org.ikankechil.iota.strategies.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stochastic Pop and Drop by Jake Bernstein and David Steckler
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:stochastic_pop_drop
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V18/C08/071POP.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StochasticPopAndDrop extends AbstractStrategy {

  // thresholds
  private static final int    BIAS       = 50;
  private static final int    WEAK_TREND = 20;
  private static final int    POP        = 80;
  private static final int    DROP       = 20;

  private static final Logger logger     = LoggerFactory.getLogger(StochasticPopAndDrop.class);

  public StochasticPopAndDrop() {
    super(new Stochastic(SEVENTY, THREE, THREE),
          new ADX(FOURTEEN),
          new Stochastic(FOURTEEN, THREE, THREE));
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
      final double longTermStochasticToday = longTermStochastic[today];
      final double adxToday = adx[today];
      // long
      if (buy(longTermStochasticToday,
              adxToday,
              shortTermStochasticYesterday,
              shortTermStochasticToday)) {
        signal = Signal.BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      // short
      else if (sell(longTermStochasticToday,
                    adxToday,
                    shortTermStochasticYesterday,
                    shortTermStochasticToday)) {
        signal = Signal.SELL;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      signals.set(date, signal, s);

      shortTermStochasticYesterday = shortTermStochasticToday;
    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    return (doubles[ZERO] > BIAS) &&      // long-term stochastic
           (doubles[ONE] < WEAK_TREND) && // ADX
           crossover(doubles[TWO], doubles[THREE], POP); // short-term stochastic
  }

  @Override
  protected boolean sell(final double... doubles) {
    return (doubles[ZERO] < BIAS) &&      // long-term stochastic
           (doubles[ONE] < WEAK_TREND) && // ADX
           crossunder(doubles[TWO], doubles[THREE], DROP); // short-term stochastic
  }

}
