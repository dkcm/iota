/**
 * MinimumPrice.java  v0.1  27 January 2015 12:54:40 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minimum Price
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumPrice extends AbstractIndicator {

  public MinimumPrice(final int period) {
    super(period, TA_LIB.minLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.min(start,
                      end,
                      values,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
