/**
 * StandardDeviation.java v0.1 15 December 2014 12:06:24 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Standard Deviation
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StandardDeviation extends AbstractIndicator {

  private final double stdDev;

  public StandardDeviation(final int period, final double stdDev) {
    super(period, TA_LIB.stdDevLookback(period, stdDev));
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
    return TA_LIB.stdDev(start,
                         end,
                         values,
                         period,
                         stdDev,
                         outBegIdx,
                         outNBElement,
                         output);
  }

}
