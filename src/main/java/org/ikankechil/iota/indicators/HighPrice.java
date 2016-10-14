/**
 * HighPrice.java  v0.1  11 September 2016 12:06:22 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HighPrice extends AbstractIndicator {

  public HighPrice() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    System.arraycopy(ohlcv.highs(), start, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
