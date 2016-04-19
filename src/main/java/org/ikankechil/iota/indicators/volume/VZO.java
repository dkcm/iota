/**
 * VZO.java	v0.1	10 December 2015 8:33:18 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume Zone Oscillator (VZO) by Walid Khalil and David Steckler
 * <p>
 * http://www.informedtrades.com/attachments/2505d1280781350-volume-analysis-volumezoneoscillator-pdf
 * http://www.traderslog.com/defining-the-trend
 * http://www.highgrowthstock.com/wpstorage/pdf/blog-hgsi-davesteckler-vzo-pzo.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VZO extends AbstractIndicator {

  public VZO() {
    this(FOURTEEN);
  }

  public VZO(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Volume zone oscillator = 100 x (VP / TV)
    // where
    // VP (Volume Position) = X-days EMA (± volume)
    // TV (Total Volume) = X-days EMA (volume)

    final double[] closes = ohlcv.closes();
    final double[] volumes = toDoubles(ohlcv.volumes());
    final int size = closes.length;

    // compute R
    final double[] r = new double[size];
    int c = ZERO;
    r[c] = volumes[c];
    for (double pc = closes[c]; ++c < size; ) {
      final double close = closes[c];
      r[c] = (close > pc) ? volumes[c] : -volumes[c];
      pc = close;
    }

    // compute volume position and total volume
    final double[] vp = ema(r, period);
    final double[] tv = ema(volumes, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = HUNDRED_PERCENT * vp[i] / tv[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
