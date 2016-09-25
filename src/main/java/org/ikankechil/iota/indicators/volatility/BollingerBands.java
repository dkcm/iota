/**
 * BollingerBands.java  v0.1  4 December 2014 12:26:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Bollinger Bands by John Bollinger
 * <p>
 * <a href="http://www.bollingerbands.com/services/bb/">Bollinger Bands Tutorial</a><br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_bands
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BollingerBands extends AbstractIndicator {

  private final double        stdDevUpper;
  private final double        stdDevLower;

  private static final String UPPER_BAND  = "Bollinger Bands Upper Band";
  private static final String MIDDLE_BAND = "Bollinger Bands Middle Band";
  private static final String LOWER_BAND  = "Bollinger Bands Lower Band";

  public BollingerBands() {
    this(TWENTY, TWO);
  }

  /**
   * @param period
   * @param stdDev standard deviation multiplier
   */
  public BollingerBands(final int period, final double stdDev) {
    this(period, stdDev, stdDev);
  }

  /**
   * @param period
   * @param stdDevUpper standard deviation multiplier for the upper band
   * @param stdDevLower standard deviation multiplier for the lower band
   */
  public BollingerBands(final int period, final double stdDevUpper, final double stdDevLower) {
    super(period, TA_LIB.bbandsLookback(period, stdDevUpper, stdDevLower, MAType.Sma));
    throwExceptionIfNegative(stdDevUpper, stdDevLower);

    // standard deviation multipliers for the upper and lower bands
    this.stdDevUpper = stdDevUpper;
    this.stdDevLower = stdDevLower;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    return generate((TimeSeries) ohlcv);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series) {
    throwExceptionIfShort(series);
    final int size = series.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outRealUpperBand = new double[size - lookback];
    final double[] outRealMiddleBand = new double[outRealUpperBand.length];
    final double[] outRealLowerBand = new double[outRealUpperBand.length];

    final RetCode outcome = TA_LIB.bbands(ZERO,
                                          size - ONE,
                                          series.values(),
                                          period,
                                          stdDevUpper,
                                          stdDevLower,
                                          MAType.Sma,
                                          outBegIdx,
                                          outNBElement,
                                          outRealUpperBand,
                                          outRealMiddleBand,
                                          outRealLowerBand);
    throwExceptionIfBad(outcome, series);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, outRealUpperBand),
                         new TimeSeries(MIDDLE_BAND, dates, outRealMiddleBand),
                         new TimeSeries(LOWER_BAND, dates, outRealLowerBand));
  }

}
