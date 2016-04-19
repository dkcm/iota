/**
 * MoneyFlowVolume.java v0.1 7 December 2015 5:19:51 PM
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
 *
 * @author Daniel Kuan
 * @version 0.1
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

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double high = highs[i];
      final double low = lows[i];
      final double close = closes[i];

      output[i] = (((close - low) - (high - close)) / (high - low)) * volumes[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
