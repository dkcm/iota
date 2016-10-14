/**
 * VolumeForce.java  v0.2  8 January 2015 10:59:56 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume Force by Stephen J. Klinger
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V15/C12/IDENTIF.pdf<br>
 * http://wiki.timetotrade.eu/Volume_Force<br>
 * http://www.motivewave.com/studies/klinger_volume_oscillator.htm<br>
 * http://www.fmlabs.com/reference/default.htm?url=KO.htm<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VolumeForce extends AbstractIndicator {

  public VolumeForce() {
    super(ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // If (high + low + close) > (high_1 + low_1 + close_1) then
    //   trend = +1
    // else
    //   trend = -1
    //
    // daily measurement, dm = high - low
    //
    // cumulative measurement, cm
    // if trend = trend_1 then
    //   cm = cm_1 + dm
    // else
    //   cm = dm_1 + dm
    //
    // vf = volume x |2 x (dm / cm) - 1| x trend x 100

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();

    int pt = ZERO, ct;      // trend
    double pdm = ZERO, cdm; // daily measurement (range)
    double pcm = ZERO, ccm; // cumulative measurement

    int t = ZERO;
    double phlc = (highs[t] + lows[t] + closes[t]);

    for (int i = ZERO; ++t < volumes.length; ++i) {
      // compute trend
      final double chlc = (highs[t] + lows[t] + closes[t]);
      ct = (chlc > phlc) ? 100 : -100;

      // compute daily measurement (range) and cumulative measurement
      cdm = highs[t] - lows[t];
      ccm = ((pt == ct) ? pcm : pdm) + cdm;

      // compute indicator
      output[i] = volumes[t] * Math.abs((TWO * cdm / ccm) - ONE) * ct;

      // shift forward in time
      phlc = chlc;
      pt = ct;
      pdm = cdm;
      pcm = ccm;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
