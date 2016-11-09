/**
 * PlusDI.java  v0.1  10 November 2016 12:22:40 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Plus Directional Indicator (+DI)
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PlusDI extends AbstractIndicator {

  public PlusDI() {
    this(FOURTEEN);
  }

  public PlusDI(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.plusDI(start,
                         end,
                         ohlcv.highs(),
                         ohlcv.lows(),
                         ohlcv.closes(),
                         period,
                         outBegIdx,
                         outNBElement,
                         output);
  }

}
