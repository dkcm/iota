/**
 * VOMA.java  v0.1  10 December 2015 10:12:08 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume-adjusted Moving Average (VOMA) by Stephan Bisse
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C02/029BISS.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C03/059BISS.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VOMA extends AbstractIndicator {

  public VOMA() {
    this(FOUR);
  }

  public VOMA(final int period) {
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
    // 1. Multiply each close by the relative absolute volume
    // 2. Sum up all volume-adjusted closes

    // compute price * volume
    final long[] volumes = ohlcv.volumes();
    final double[] priceVolumes = new double[volumes.length];
    for (int i = ZERO; i < priceVolumes.length; ++i) {
      priceVolumes[i] = ohlcv.close(i) * volumes[i];
    }

    // compute sums
    final double[] sumPriceVolumes = sum(period, priceVolumes);
    final double[] sumVolumes = sum(period, toDoubles(volumes));

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = sumPriceVolumes[i] / sumVolumes[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
