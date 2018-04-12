/**
 * MVWAP.java  v0.1  10 April 2018 11:51:27 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.MA;
import org.ikankechil.iota.indicators.trend.SMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Moving volume-weighted average price (MVWAP)
 *
 * <p>References:
 * <li>http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:vwap_intraday<br>
 * <li>https://en.wikipedia.org/wiki/Volume-weighted_average_price<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MVWAP extends AbstractIndicator {

  private final MA          ma;

  private static final VWAP VWAP = new VWAP();

  public MVWAP(final int period) {
    this(period, null);
  }

  public MVWAP(final int period, final MA ma) {
    super(period, period - ONE);

    this.ma = (ma != null) ? ma : new SMA(period); // defaults to SMA
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. compute VWAP
    // 2. MVWAP = moving average of VWAP

    final TimeSeries vwap = VWAP.generate(ohlcv).get(ZERO);
    final TimeSeries mvwap = ma.generate(vwap).get(ZERO);
    System.arraycopy(mvwap.values(), ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
