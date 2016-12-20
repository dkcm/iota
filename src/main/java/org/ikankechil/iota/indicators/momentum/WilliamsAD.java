/**
 * WilliamsAD.java  0.1  20 December 2016 5:01:07 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Williams Accumulation Distribution by Larry Williams (and modified by Steven Achelis)
 *
 * <p>https://www.incrediblecharts.com/indicators/williams_accumulate_distribute.php<br>
 * https://www.incrediblecharts.com/indicators/williams_accumulation_distribution.php<br>
 * http://exceltechnical.web.fc2.com/wad.html<br>
 * http://www.stockfetcher.com/forums2/Indicators/Williams-Accumulation-Distribution/76455<br>
 * http://www.technicalindicators.net/indicators-technical-analysis/122-wad-williams-accumulation-distribution<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WilliamsAD extends AbstractIndicator {

  public WilliamsAD() {
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
    // wmax = max(high,close 1 day ago)
    // wmin = min(low, close 1 day ago)
    //
    // if (close > close 1 day ago) wval = close - wmin
    // else if (close < close 1 day ago) wval = close - wmax
    // else wval = 0
    //
    // Williams Accumulation Distribution = Williams Accumulation Distribution 1 day ago + wval

    // compute indicator
    int i = ZERO;
    double pc = ohlcv.close(i);
    double w = ZERO;
    double wad = output[i] = w;

    while (++i < output.length) {
      final double close = ohlcv.close(i);

      if (close > pc) {
        w = close - Math.min(ohlcv.low(i), pc);
      }
      else if (close < pc) {
        w = close - Math.max(ohlcv.high(i), pc);
      }
      else {
        w = ZERO;
      }
      output[i] = wad += w;

      // shift forward in time
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
