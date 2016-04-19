/**
 * ParabolicSAR.java v0.1  4 December 2014 1:05:02 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Parabolic SAR by Welles Wilder
 * <p>
 * http://stockcharts.com/school/doku.php?st=sar&id=chart_school:technical_indicators:parabolic_sar
 *
 * @author Daniel Kuan
 * @version
 */
public class ParabolicSAR extends AbstractIndicator {

  private final double acceleration;
  private final double maximum;

  public ParabolicSAR() {
    this(0.02, 0.20);
  }

  public ParabolicSAR(final double acceleration, final double maximum) {
    super(TA_LIB.sarLookback(acceleration, maximum));
    throwExceptionIfNegative(acceleration, maximum);

    this.acceleration = acceleration;
    this.maximum = maximum;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.sar(start,
                      end,
                      ohlcv.highs(),
                      ohlcv.lows(),
                      acceleration,
                      maximum,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
