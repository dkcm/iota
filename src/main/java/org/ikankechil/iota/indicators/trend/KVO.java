/**
 * KVO.java  v0.1  8 January 2015 2:04:34 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Klinger Volume Oscillator (KVO) by Stephen J. Klinger
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V15/C12/IDENTIF.pdf<br>
 * http://www.fmlabs.com/reference/default.htm?url=KO.htm<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KVO extends IndicatorWithSignalLine {

  private static final VolumeForce VF       = new VolumeForce();

  private static final int         EMA34    = 34;
  private static final int         EMA55    = 55;

  private static final int         LOOKBACK = TA_LIB.emaLookback(EMA55) + VF.lookback() + TA_LIB.emaLookback(THIRTEEN);

  public KVO() {
    super(THIRTEEN, LOOKBACK);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // KVO = EMA34(VF) - EMA55(VF)

    // compute volume force
    final double[] vf = VF.generate(ohlcv).get(ZERO).values();

    // compute EMAs
    final double[] ema34 = ema(vf, EMA34);
    final double[] ema55 = ema(vf, EMA55);

    // compute indicator
    for (int i = ZERO, j = ema34.length - ema55.length; i < output.length; ++i, ++j) {
      output[i] = ema34[j] - ema55[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
