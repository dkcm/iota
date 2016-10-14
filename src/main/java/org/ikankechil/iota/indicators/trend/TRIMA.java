/**
 * TRIMA.java  v0.1  5 January 2015 7:06:34 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Triangular Moving Average (TRIMA)
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TRIMA extends AbstractIndicator {

  public TRIMA(final int period) {
    super(period, TA_LIB.trimaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.trima(start,
                        end,
                        values,
                        period,
                        outBegIdx,
                        outNBElement,
                        output);
  }

}
