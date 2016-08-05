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
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class VOMA extends AbstractIndicator {

  public VOMA() {
    this(FOUR);
  }

  public VOMA(final int period) {
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


    // compute indicator


    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
