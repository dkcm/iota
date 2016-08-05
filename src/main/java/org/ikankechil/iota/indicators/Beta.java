/**
 * Beta.java  v0.1  8 December 2014 8:40:50 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class Beta extends AbstractIndicator {

  public Beta(final int period) {
    super(period, TA_LIB.betaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.beta(start,
                       end,
                       ohlcv.closes(), // TODO choose something else
                       ohlcv.closes(),
                       period,
                       outBegIdx,
                       outNBElement,
                       output);
  }

}
