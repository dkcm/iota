/**
 * ClosePrice.java  v0.1  10 September 2016 11:52:15 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ClosePrice extends AbstractIndicator {

  public ClosePrice() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    System.arraycopy(values, start, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
