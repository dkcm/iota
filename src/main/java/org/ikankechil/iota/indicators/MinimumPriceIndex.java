/**
 * MinimumPriceIndex.java  v0.1  27 January 2015 1:05:56 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minimum Price Index
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumPriceIndex extends AbstractIndicator {

  public MinimumPriceIndex(final int period) {
    super(period, TA_LIB.minIndexLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final int outInteger[] = new int[output.length];
    final RetCode outcome = TA_LIB.minIndex(start,
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
