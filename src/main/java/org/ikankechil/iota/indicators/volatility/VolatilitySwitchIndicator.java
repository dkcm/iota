/**
 * VolatilitySwitchIndicator.java  v0.1  13 April 2018 9:36:44 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;

/**
 * Volatility Switch Indicator by Ron McEwan
 *
 * <p>References:
 * <li>http://edmond.mires.co/GES816/60-The%20Volatility%20(Regime)%20Switch%20Indicator.pdf<br>
 * <li>http://traders.com/Documentation/FEEDbk_docs/2013/02/TradersTips.html<br>
 * <li>http://www.traders.com/Documentation/FEEDbk_docs/2013/02/volt-switch.xls<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolatilitySwitchIndicator extends AbstractIndicator {

  private final double            volatilityThreshold;
  private final double            inversePeriod;
  private final StandardDeviation stdDev;

  public VolatilitySwitchIndicator() {
    this(TWENTY_ONE, HALF);
  }

  public VolatilitySwitchIndicator(final int period, final double volatilityThreshold) {
    super(period, (period << ONE) - ONE);
    throwExceptionIfNegative(volatilityThreshold);

    this.volatilityThreshold = volatilityThreshold;
    stdDev = new StandardDeviation(period);
    inversePeriod = ONE / (double) period;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    // Formula:
    // 1. compute daily change = (c - pc) / ((c + pc) / 2)
    // 2. compute historical volatility = std dev
    // 3. compute volatility switch indicator

    throwExceptionIfShort(series);
    final int size = series.size();

    final double[] dailyChanges = computeDailyChange(series.values());
    final double[] historicalVolatility = computeHistoricalVolatility(dailyChanges);

    // compute indicator
    final double[] indicator = computeVolatilitySwitch(historicalVolatility);

    final double[] constant = new double[indicator.length];
    Arrays.fill(constant, volatilityThreshold);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name, dates, indicator),
                         new TimeSeries(name, dates, constant));
  }

  private static double[] computeDailyChange(final double... closes) {
    final double[] dailyChanges = new double[closes.length - ONE];

    int i = ZERO;
    double pc = closes[i];
    for (int j = ZERO; j < dailyChanges.length; ++j) {
      final double c = closes[++i];
      dailyChanges[j] = TWO * (c - pc) / (c + pc);

      // shift forward in time
      pc = c;
    }

    return dailyChanges;
  }

  private double[] computeHistoricalVolatility(final double... dailyChanges) {
    final double[] historicalVolatility = new double[dailyChanges.length - stdDev.lookback()];
    stdDev.compute(ZERO, dailyChanges.length, dailyChanges, new MInteger(), new MInteger(), historicalVolatility);
    return historicalVolatility;
  }

  private double[] computeVolatilitySwitch(final double... historicalVolatility) {
    final int offset = period - ONE;
    final double[] indicator = new double[historicalVolatility.length - offset];

    for (int i = ZERO, j = offset; i < indicator.length; ++i, ++j) {
      final double threshold = historicalVolatility[j];
      int count = ONE;
      for (int k = i; k < j; ++k) {
        if (historicalVolatility[k] <= threshold) {
          ++count;
        }
      }
      indicator[i] = count * inversePeriod;
    }

    return indicator;
  }

}
