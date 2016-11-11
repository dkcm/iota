/**
 * DX.java  v0.2  15 December 2014 3:14:55 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Directional Movement Index (DX)
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DX extends AbstractIndicator {

  private final PlusDI  plusDI;
  private final MinusDI minusDI;

  public DX() {
    this(FOURTEEN);
  }

  public DX(final int period) {
    super(period, period);

    plusDI = new PlusDI(period);
    minusDI = new MinusDI(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // compute DIs
    final double[] plusDIs = plusDI.generate(ohlcv).get(ZERO).values();
    final double[] minusDIs = minusDI.generate(ohlcv).get(ZERO).values();

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double pdi = plusDIs[i];
      final double mdi = minusDIs[i];
      output[i] = HUNDRED_PERCENT * Math.abs(pdi - mdi) / (pdi + mdi);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
