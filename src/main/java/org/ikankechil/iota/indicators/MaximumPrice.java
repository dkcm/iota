/**
 * MaximumPrice.java v0.1 27 January 2015 12:52:10 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Maximum Price
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MaximumPrice extends AbstractIndicator {

  public MaximumPrice(final int period) {
    super(period, TA_LIB.maxLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.max(start,
                      end,
                      values,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
