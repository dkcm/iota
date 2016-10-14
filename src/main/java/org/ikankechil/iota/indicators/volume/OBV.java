/**
 * OBV.java  v0.2  4 December 2014 1:17:27 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * On Balance Volume (OBV) by Joe Granville
 *
 * <p>http://stockcharts.com/school/doku.php?st=obv&id=chart_school:technical_indicators:on_balance_volume_obv<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OBV extends AbstractIndicator {

  public OBV() {
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
    // If the closing price is above the prior close price then:
    // Current OBV = Previous OBV + Current Volume
    //
    // If the closing price is below the prior close price then:
    // Current OBV = Previous OBV - Current Volume
    //
    // If the closing prices equals the prior close price then:
    // Current OBV = Previous OBV (no change)

    // compute indicator
    int i = ZERO;
    double pc = ohlcv.close(i);
    double pobv = output[i] = ohlcv.volume(i);

    while (++i < output.length) {
      final double close = ohlcv.close(i);

      if (close > pc) {
        pobv += ohlcv.volume(i);
      }
      else if (close < pc) {
        pobv -= ohlcv.volume(i);
      }
      output[i] = pobv;

      // shift forward in time
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
