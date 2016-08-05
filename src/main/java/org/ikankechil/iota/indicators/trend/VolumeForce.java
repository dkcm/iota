/**
 * VolumeForce.java  v0.1 8 January 2015 10:59:56 AM
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
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V15/C12/IDENTIF.pdf
 * http://wiki.timetotrade.eu/Volume_Force
 * http://www.motivewave.com/studies/klinger_volume_oscillator.htm
 * http://www.fmlabs.com/reference/default.htm?url=KO.htm
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolumeForce extends AbstractIndicator {

  public VolumeForce() {
    super(TWO);
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

    // compute trend
    final int[] trend = new int[ohlcv.size() - ONE];
    double phlc = (highs[ZERO] + lows[ZERO] + closes[ZERO]);
    for (int i = ZERO, j = ONE; i < trend.length; ++i, ++j) {
      final double chlc = (highs[j] + lows[j] + closes[j]);
      trend[i] = (chlc > phlc) ? 100 : -100;
      phlc = chlc;
    }

    // compute daily measurement (range)
    final double[] dm = new double[trend.length];
    for (int i = ZERO; i < dm.length; ++i) {
      dm[i] = highs[i] - lows[i];
    }

    // compute cumulative measurement
    final double[] cm = new double[dm.length - ONE];
    cm[ZERO] = dm[ZERO];  // seed
    for (int today = ONE, yesterday = ZERO; today < cm.length; ++today, ++yesterday) {
      cm[today] = ((trend[today] == trend[yesterday]) ? cm[yesterday]
                                                      : dm[yesterday]) + dm[today];
    }

    // compute indicator
    final long[] volumes = ohlcv.volumes();
    // cm.length = trend.length - 1 = ohlcv.size() - 2
    for (int i = ZERO, j = ONE, k = TWO; i < output.length; ++i, ++j, ++k) {
      output[i] = volumes[k] * Math.abs((TWO * dm[j] / cm[i]) - ONE) * trend[j];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
