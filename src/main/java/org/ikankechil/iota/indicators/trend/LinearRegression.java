/**
 * LinearRegression.java  v0.1  10 December 2014 10:24:29 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Linear Regression
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegression extends AbstractIndicator {

  public LinearRegression() {
    this(FOURTEEN);
  }

  public LinearRegression(final int period) {
    super(period, TA_LIB.linearRegLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.linearReg(start,
                            end,
                            values,
                            period,
                            outBegIdx,
                            outNBElement,
                            output);
  }

}
