/**
 * APTR.java  v0.1  24 April 2018 9:05:25 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.MA;
import org.ikankechil.iota.indicators.trend.WildersSmoothing;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average Percentage True Range (APTR) by Vitali Apirine
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2015/11/TradersTips.html
 * <li>http://tlc.tdameritrade.com.sg/center/reference/Tech-Indicators/studies-library/A-B/APTR.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class APTR extends AbstractIndicator {

  private final MA ma;

  public APTR() {
    this(FOURTEEN);
  }

  public APTR(final int period) {
    this(new WildersSmoothing(period));
  }

  public APTR(final MA ma) {
    super(ma.lookback() + ONE);

    this.ma = ma;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. divide true range by its own middle value, which yields the target percent ratio
    // 2. smooth ratio by a moving average (Wilder's smoothing by default)

    final double[] ptrs = new double[ohlcv.size() - ONE];
    for (int i = ZERO; i < ptrs.length; ) {
      final double close = ohlcv.close(i);
      ptrs[i] = percentageTrueRange(ohlcv.high(++i), ohlcv.low(i), close);
    }

    // compute indicator
    final double[] aptrs = ma.generate(new TimeSeries(EMPTY, new String[ptrs.length], ptrs)).get(ZERO).values();
    System.arraycopy(aptrs, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  static final double percentageTrueRange(final double highToday, final double lowToday, final double closeYesterday) {
    final double trueRange;
    final double low;
    if (closeYesterday >= highToday) {
      trueRange = closeYesterday - lowToday;
      low = lowToday;
    }
    else if (closeYesterday <= lowToday) {
      trueRange = highToday - closeYesterday;
      low = closeYesterday;
    }
    else {
      trueRange = highToday - lowToday;
      low = lowToday;
    }
    final double middle = low + (trueRange * HALF);
    return trueRange / middle * HUNDRED_PERCENT;
  }

}
