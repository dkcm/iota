/**
 * PVT.java  0.1  19 December 2016 1:20:52 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Price and Volume Trend (PVT)
 *
 * <p>https://www.tradingview.com/wiki/Price_Volume_Trend_(PVT)#DEFINITION<br>
 * https://www.incrediblecharts.com/indicators/price_and_volume_trend.php<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PVT extends AbstractIndicator {

  public PVT() {
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
    // PVT = [((CurrentClose - PreviousClose) / PreviousClose) x Volume] + PreviousPVT

    // compute indicator
    int i = ZERO;
    double pc = ohlcv.close(i);
    double pvt = output[i] = ohlcv.volume(i);

    while (++i < output.length) {
      final double close = ohlcv.close(i);
      output[i] = pvt += (close / pc - ONE) * ohlcv.volume(i);

      // shift forward in time
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
