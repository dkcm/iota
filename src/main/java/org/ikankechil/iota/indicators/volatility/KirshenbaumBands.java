/**
 * KirshenbaumBands.java  v0.1  12 January 2015 11:39:32 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kirshenbaum Bands
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class KirshenbaumBands extends AbstractIndicator {

  private final int           ema;
  private final double        deviations;
  private final int           emaLookback;
  private final int           linearRegLookback;

  private static final double KIRSHENBAUM_DEVS = 1.75;

  private static final String UPPER_BAND       = "Kirshenbaum Bands Upper Band";
  private static final String MIDDLE_BAND      = "Kirshenbaum Bands Middle Band";
  private static final String LOWER_BAND       = "Kirshenbaum Bands Lower Band";

  public KirshenbaumBands() {
    this(NINE, FIVE, KIRSHENBAUM_DEVS);
  }

  public KirshenbaumBands(final int ema, final int regression, final double deviations) {
    super(regression, Math.max(TA_LIB.emaLookback(ema), TA_LIB.linearRegLookback(regression)));
    throwExceptionIfNegative(ema, deviations);

    this.ema = ema;
    this.deviations = deviations;
    emaLookback = TA_LIB.emaLookback(ema);
    linearRegLookback = TA_LIB.linearRegLookback(regression);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) { // TODO incomplete
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

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // compute EMA
    final double[] emas = new double[size - emaLookback];
    RetCode outcome = TA_LIB.ema(ZERO,
                                 size - ONE,
                                 closes,
                                 ema,
                                 outBegIdx,
                                 outNBElement,
                                 emas);
    throwExceptionIfBad(outcome, ohlcv);

    // compute linear regression
    final double[] lrs = new double[size - linearRegLookback];
    outcome = TA_LIB.linearReg(ZERO,
                               size - ONE,
                               closes,
                               period,
                               outBegIdx,
                               outNBElement,
                               lrs);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] upperBand = new double[size - lookback];
    final double[] middleBand = new double[upperBand.length];
    final double[] lowerBand = new double[upperBand.length];

    for (int i = ZERO; i < middleBand.length; ++i) {
      final double bandWidth = deviations * 1;
      final double mid = middleBand[i];
      upperBand[i] = mid + bandWidth;
      lowerBand[i] = mid - bandWidth;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
