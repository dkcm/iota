/**
 * Range.java  v0.1 25 November 2015 9:54:20 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Range extends AbstractIndicator {

  public Range() {
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

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = highs[i] - lows[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
