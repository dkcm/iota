/**
 * MinusDI.java  v0.1  10 November 2016 12:25:38 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minus Directional Indicator (-DI)
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinusDI extends AbstractIndicator {

  public MinusDI() {
    this(FOURTEEN);
  }

  public MinusDI(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.minusDI(start,
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
