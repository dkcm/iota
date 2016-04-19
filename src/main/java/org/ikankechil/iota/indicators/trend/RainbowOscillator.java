/**
 * RainbowOscillator.java	v0.1	1 March 2015 5:57:35 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Rainbow Oscillator
 * <p>
 * http://www.amibroker.com/library/detail.php?id=101
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class RainbowOscillator extends RainbowCharts {

  private final int           minMaxLookback;

  private static final int    MIN_MAX_PERIOD = TEN;

  private static final String UPPER_BAND     = "Rainbow Upper Band";
  private static final String MIDDLE_BAND    = "Rainbow Oscillator";
  private static final String LOWER_BAND     = "Rainbow Lower Band";

  public RainbowOscillator() {
    this(TWO);
  }

  public RainbowOscillator(final int period) {
    super(period);

    minMaxLookback = TA_LIB.minMaxLookback(MIN_MAX_PERIOD);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    final List<TimeSeries> rainbow = super.generate(ohlcv);

    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    // compute llv and hhv
    final double[] llv = new double[size - minMaxLookback];
    final double[] hhv = new double[llv.length];

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final RetCode outcome = TA_LIB.minMax(ZERO,
                                          size - ONE,
                                          closes,
                                          MIN_MAX_PERIOD,
                                          outBegIdx,
                                          outNBElement,
                                          llv,
                                          hhv);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] indicator = new double[rainbow.get(ZERO).size()];
    final double[] upperBand = new double[indicator.length];
    final double[] lowerBand = new double[upperBand.length];

    for (int i = ZERO; i < indicator.length; ++i) {
      final double max = 0; // TODO incomplete
      final double min = 0;
      final double range = 100 / (hhv[0] - llv[0]);

      upperBand[i] = (max - min) * range;
      lowerBand[i] = -upperBand[i];

      final double ave = 0 / TEN;
      indicator[i] = (closes[0] - ave) * range;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);

    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, indicator),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
