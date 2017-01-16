/**
 * TypicalPrice.java  v0.3  12 January 2015 11:15:41 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Typical Price
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class TypicalPrice extends AbstractIndicator {

  public TypicalPrice() {
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
    final double[] closes = ohlcv.closes();

    for (int i = ZERO; i < output.length; ++i) {
      output[i] = typicalPrice(highs[i], lows[i], closes[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  public static final double typicalPrice(final double high, final double low, final double close) {
    return (high + low + close) * THIRD;
  }

}
