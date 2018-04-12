/**
 * MoneyFlowVolume.java  v0.3  7 December 2015 5:19:51 PM
 *
 * Copyright © 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Money Flow Volume
 *
 * <p>References:
 * <li>https://www.tradingview.com/stock-charts-support/index.php/Money_Flow_Volume<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MoneyFlowVolume extends AbstractIndicator {

  public MoneyFlowVolume() {
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
    // 1. Money Flow Multiplier = [(Close - Low) - (High - Close)] / (High - Low)
    // 2. Money Flow Volume = Money Flow Multiplier x Volume for the Period

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double high = ohlcv.high(i);
      final double low = ohlcv.low(i);
      final double range = high - low;
      if (range > ZERO) {
        output[i] = mfv(high, low, ohlcv.close(i), range, ohlcv.volume(i));
      }
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  double mfv(final double high,
             final double low,
             final double close,
             final double range,
             final long volume) {
    return (((close - low) - (high - close)) / range) * volume;
  }

}
