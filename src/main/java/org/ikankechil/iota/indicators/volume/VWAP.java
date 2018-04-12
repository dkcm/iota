/**
 * VWAP.java  v0.1  10 April 2018 11:47:14 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import static org.ikankechil.iota.indicators.TypicalPrice.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume-weighted average price (VWAP)
 *
 * <p>References:
 * <li>http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:vwap_intraday<br>
 * <li>https://en.wikipedia.org/wiki/Volume-weighted_average_price<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWAP extends AbstractIndicator {

  public VWAP() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. calculate typical price
    // 2. TPV = typical price x volume
    // 3. compute cumulative TPV
    // 4. compute cumulative volume
    // 5. VWAP = cumulative TPV / cumulative volume

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();

    // compute indicator
    double ctpv = ZERO;
    long cvolume = ZERO;
    for (int i = ZERO; i < output.length; ++i) {
      final long volume = volumes[i];
      final double tpv = typicalPrice(highs[i], lows[i], closes[i]) * volume;

      // compute cumulatives
      ctpv += tpv;
      cvolume += volume;

      output[i] = ctpv / cvolume;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
