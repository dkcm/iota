/**
 * PlusDM.java  v0.1  10 November 2016 12:19:38 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Plus Directional Movement (+DM)
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PlusDM extends AbstractIndicator {

  public PlusDM() {
    this(FOURTEEN);
  }

  public PlusDM(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.plusDM(start,
                         end,
                         ohlcv.highs(),
                         ohlcv.lows(),
                         period,
                         outBegIdx,
                         outNBElement,
                         output);
  }

}
