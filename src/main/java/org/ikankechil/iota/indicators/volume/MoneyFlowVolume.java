/**
 * MoneyFlowVolume.java  v0.2 7 December 2015 5:19:51 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Money Flow Volume
 * <p>
 * https://www.tradingview.com/stock-charts-support/index.php/Money_Flow_Volume
 *
 * @author Daniel Kuan
 * @version 0.2
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
        final double close = ohlcv.close(i);
        output[i] = (((close - low) - (high - close)) / range) * ohlcv.volume(i);
      }
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
