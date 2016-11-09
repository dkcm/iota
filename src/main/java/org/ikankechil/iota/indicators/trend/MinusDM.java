/**
 * MinusDM.java  v0.1  10 November 2016 12:25:17 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minus Directional Movement (-DM)
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinusDM extends AbstractIndicator {

  public MinusDM() {
    this(FOURTEEN);
  }

  public MinusDM(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.minusDM(start,
                          end,
                          ohlcv.highs(),
                          ohlcv.lows(),
                          period,
                          outBegIdx,
                          outNBElement,
                          output);
  }

}
