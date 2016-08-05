/**
 * MOMA.java  v0.1  16 July 2015 12:26:46 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Move-adjusted Moving Average (MOMA) by Stephan Bisse
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class MOMA extends AbstractIndicator {

  public MOMA() {
    this(FOUR);
  }

  public MOMA(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:

    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();


    // compute indicator


    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
