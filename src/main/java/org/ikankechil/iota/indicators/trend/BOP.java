/**
 * BOP.java  v0.1 10 December 2014 9:14:07 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Balance Of Power (BOP)
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BOP extends AbstractIndicator {

  public BOP() {
    super(TA_LIB.bopLookback());
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.bop(start,
                      end,
                      ohlcv.opens(),
                      ohlcv.highs(),
                      ohlcv.lows(),
                      ohlcv.closes(),
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
