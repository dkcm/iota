/**
 * MaximumPriceIndex.java v0.1 27 January 2015 1:00:17 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Maximum Price Index
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MaximumPriceIndex extends AbstractIndicator {

  public MaximumPriceIndex(final int period) {
    super(period, TA_LIB.maxIndexLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final int outInteger[] = new int[output.length];
    final RetCode outcome = TA_LIB.maxIndex(start,
                                            end,
                                            values,
                                            period,
                                            outBegIdx,
                                            outNBElement,
                                            outInteger);
    for (int i = ZERO; i < outInteger.length; ++i) {
      output[i] = outInteger[i];
    }
    return outcome;
  }

}
