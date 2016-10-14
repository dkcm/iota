/**
 * Variance.java  v0.1  9 December 2014 12:52:05 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Variance
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Variance extends AbstractIndicator {

  private final double stdDev;

  public Variance(final int period, final double stdDev) {
    super(period, TA_LIB.varianceLookback(period, stdDev));
    throwExceptionIfNegative(stdDev);

    this.stdDev = stdDev;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.variance(start,
                           end,
                           values,
                           period,
                           stdDev,
                           outBegIdx,
                           outNBElement,
                           output);
  }

}
