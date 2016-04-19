/**
 * MedianPrice.java	v0.2	12 January 2015 11:12:46 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Median Price
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MedianPrice extends AbstractIndicator {

  public MedianPrice() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    for (int i = ZERO; i < output.length; ++i) {
      output[i] = (highs[i] + lows[i]) * HALF;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
