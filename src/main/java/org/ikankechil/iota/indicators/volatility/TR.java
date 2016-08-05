/**
 * TR.java  v0.2  15 December 2014 2:21:16 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * True Range (TR)
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TR extends AbstractIndicator {

  public TR() {
    super(ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // True range:
    // Greatest distance between:
    // a) today's high and today's low
    // b) today's high and yesterday's close
    // c) today's low and yesterday's close
    //
    // Alternative:
    // max(today's high and yesterday's close) - min(today's low and yesterday's close)

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    // compute indicator
    int i = ZERO;
    double close = closes[i];
    for (; i < output.length; ) {
      output[i++] = max(highs[i], close) - min(lows[i], close);
      close = closes[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;

  }

  private static final double max(final double a, final double b) {
    return (a >= b) ? a : b;
  }

  private static double min(final double a, final double b) {
    return (a <= b) ? a : b;
  }

}
