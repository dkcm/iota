/**
 * DX.java  v0.1  15 December 2014 3:14:55 PM
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
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DX extends AbstractIndicator {

  public DX() {
    this(FOURTEEN);
  }

  public DX(final int period) {
    super(period, TA_LIB.dxLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.dx(start,
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
