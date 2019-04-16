/**
 * KirshenbaumBands.java  v0.2  12 January 2015 11:39:32 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import static java.lang.Math.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.LinearRegression;

/**
 * Kirshenbaum Bands by Paul Kirshenbaum
 *
 * <p>References:
 * <li>https://www.tradingview.com/script/dBTwZawK-Kirshenbaum-Bands/
 * <li>https://user42.tuxfamily.org/chart/manual/Kirshenbaum-Bands.html
 * <li>https://www.motivewave.com/studies/kirshenbaum_bands.htm<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
class KirshenbaumBands extends AbstractIndicator {

  private final int           ema;
  private final double        deviations;
  private final int           regression;
  private final Indicator     linearRegression;

  private static final double DEVIATIONS  = 1.75;

  private static final String UPPER_BAND  = "Kirshenbaum Bands Upper Band";
  private static final String MIDDLE_BAND = "Kirshenbaum Bands Middle Band";
  private static final String LOWER_BAND  = "Kirshenbaum Bands Lower Band";

  public KirshenbaumBands() {
    this(NINE, FIVE, DEVIATIONS);
  }

  public KirshenbaumBands(final int ema, final int regression, final double deviations) {
    super(regression, Math.max(ema - ONE, regression - ONE));
    throwExceptionIfNegative(ema, regression, deviations);

    this.ema = ema;
    this.deviations = deviations;
    this.regression = regression;
    linearRegression = new LinearRegression(regression);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) { // TODO incomplete
    // 1. Calculate a P-Period Exponential Moving Average of the data based on
    //    the close.
    // 2. Then, for each bar, calculate the L-Period linear regression line,
    //    using today’s close as the endpoint of the line.
    // 3. Calculate d1, d2, d3, .. dL as the distance from the line to the close
    //    on each bar which was used to derive the line.
    //  That is, di = Distance from Regression Line to each bar’s close.
    //  Calculate the average of the squared errors:
    //  AE = (d12 + d22 + d32 + .. + dN2) / L
    //
    //  Standard Error, Se = square root of AE
    //
    //  Then, if N = Number of Standard Errors, band width is:
    //  BW = N * SE
    //
    // Add and subtract the band width from the Exponential Moving Average to
    // arrive at today’s value for the upper and lower bands.
    //
    // Parameters:
    // Periods (P): The period used in the Exponential Moving Average
    //              calculation.
    // Linear Regression Periods (L): The period used in constructing the lines
    //                                for Linear Regression.
    // Deviations (N): Number of deviations used. That is, the Standard Error
    //                 value can be multiplied by a factor to expand the bands.
    //                 Mr. Kirshenbaum recommends a value of 1.75.

    throwExceptionIfShort(series);
    final int size = series.size();

    // compute EMA
    final double[] emas = ema(series.values(), ema);

    // compute linear regression
    final double[] lrs = linearRegression.generate(series, start).get(ZERO).values();

    // compute average errors
    final double[] ds = difference(Arrays.copyOfRange(series.values(),
                                                      linearRegression.lookback(),
                                                      size),
                                   lrs);
    final double[] averageErrors = sma(ds, regression);

    // compute indicator
    final double[] upperBand = new double[size - lookback];
    final double[] middleBand = new double[upperBand.length];
    final double[] lowerBand = new double[upperBand.length];

    for (int i = ZERO; i < middleBand.length; ++i) {
      final double mid = middleBand[i];
      final double bandwidth = deviations * sqrt(averageErrors[i]);
      upperBand[i] = mid + bandwidth;
      lowerBand[i] = mid - bandwidth;
    }

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
