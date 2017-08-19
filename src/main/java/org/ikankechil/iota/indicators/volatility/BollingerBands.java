/**
 * BollingerBands.java  v0.3  4 December 2014 12:26:06 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Bollinger Bands by John Bollinger
 *
 * <p>References:
 * <li><a href="http://www.bollingerbands.com/services/bb/">Bollinger Bands Tutorial</a><br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_bands<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class BollingerBands extends AbstractIndicator {

  private final Indicator     stdDevUpper;
  private final Indicator     stdDevLower;

  private static final String UPPER_BAND  = "Bollinger Bands Upper Band";
  private static final String MIDDLE_BAND = "Bollinger Bands Middle Band";
  private static final String LOWER_BAND  = "Bollinger Bands Lower Band";

  public BollingerBands() {
    this(TWENTY, TWO);
  }

  /**
   *
   *
   * @param period
   * @param stdDev standard deviation multiplier
   */
  public BollingerBands(final int period, final double stdDev) {
    this(period, stdDev, stdDev);
  }

  /**
   *
   *
   * @param period
   * @param stdDevUpper standard deviation multiplier for the upper band
   * @param stdDevLower standard deviation multiplier for the lower band
   */
  public BollingerBands(final int period, final double stdDevUpper, final double stdDevLower) {
    super(period, period - ONE);
    throwExceptionIfNegative(stdDevUpper, stdDevLower);

    // standard deviation multipliers for the upper and lower bands
    this.stdDevUpper = new StandardDeviation(period, stdDevUpper);
    this.stdDevLower = (stdDevUpper == stdDevLower) ?
                       this.stdDevUpper :
                       new StandardDeviation(period, stdDevLower);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    return generate((TimeSeries) ohlcv);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series) {
    // Formula:
    // Middle Band = 20-day simple moving average (SMA)
    // Upper Band = 20-day SMA + (20-day standard deviation of price x 2)
    // Lower Band = 20-day SMA - (20-day standard deviation of price x 2)
    throwExceptionIfShort(series);

    final double[] middleBand = sma(series.values(), period);
    final double[] upperBand = stdDevUpper.generate(series).get(ZERO).values();
    final double[] lowerBand = (stdDevUpper == stdDevLower) ?
                               Arrays.copyOf(upperBand, upperBand.length) :
                               stdDevLower.generate(series).get(ZERO).values();

    for (int i = ZERO; i < middleBand.length; ++i) {
      final double middle = middleBand[i];
      upperBand[i] += middle;
      lowerBand[i] = middle - lowerBand[i];
    }

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, series.size());

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
