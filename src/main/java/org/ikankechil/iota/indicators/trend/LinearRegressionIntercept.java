/**
 * LinearRegressionIntercept.java  v0.1  5 January 2015 9:58:35 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionIntercept extends AbstractIndicator {

  public LinearRegressionIntercept() {
    this(FOURTEEN);
  }

  public LinearRegressionIntercept(final int period) {
    super(period, TA_LIB.linearRegInterceptLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.linearRegIntercept(start,
                                     end,
                                     values,
                                     period,
                                     outBegIdx,
                                     outNBElement,
                                     output);
  }

}
