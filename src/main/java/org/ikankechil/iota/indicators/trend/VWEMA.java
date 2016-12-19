/**
 * VWEMA.java  0.1  19 December 2016 2:46:40 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume-Weighted EMA (VWEMA) by David Hawkins
 *
 * <p>http://traders.com/Documentation/FEEDbk_docs/2009/10/Hawkins.html<br>
 * http://traders.com/Documentation/FEEDbk_docs/2009/10/TradersTips.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWEMA extends AbstractIndicator {

  public VWEMA(final int period) {
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
    // VWEMA = EMA(volume * close) / EMA(volume)

    // compute price * volume
    final double[] volumes = toDoubles(ohlcv.volumes());
    final double[] priceVolumes = new double[volumes.length];
    for (int i = ZERO; i < priceVolumes.length; ++i) {
      priceVolumes[i] = ohlcv.close(i) * volumes[i];
    }

    // compute EMAs
    final double[] emaPriceVolumes = ema(priceVolumes, period);
    final double[] emaVolumes = ema(volumes, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = emaPriceVolumes[i] / emaVolumes[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
