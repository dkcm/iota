/**
 * TR.java  v0.3  15 December 2014 2:21:16 PM
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
 *
 * <p>http://user42.tuxfamily.org/chart/manual/True-Range.html<br>
 * http://www.macroption.com/true-range/<br>
 *
 * @author Daniel Kuan
 * @version 0.3
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

    // compute indicator
    int i = ZERO;
    double close = ohlcv.close(i);
    while (i < output.length) {
      output[i++] = trueRange(ohlcv.high(i), ohlcv.low(i), close);
      close = ohlcv.close(i);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  public static final double trueRange(final double highToday, final double lowToday, final double closeYesterday) {
    final double trueRange;
    if (closeYesterday >= highToday) {
      trueRange = closeYesterday - lowToday;
    }
    else if (closeYesterday <= lowToday) {
      trueRange = highToday - closeYesterday;
    }
    else {
      trueRange = highToday - lowToday;
    }
    return trueRange;
  }

}
