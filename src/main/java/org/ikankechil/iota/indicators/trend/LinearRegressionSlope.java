/**
 * LinearRegressionSlope.java  v0.1  20 December 2014 12:14:53 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Type description goes here.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionSlope extends AbstractIndicator {

  public LinearRegressionSlope() {
    this(FOURTEEN);
  }

  public LinearRegressionSlope(final int period) {
    super(period, TA_LIB.linearRegSlopeLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.linearRegSlope(start,
                                 end,
                                 values,
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 output);
  }

}
