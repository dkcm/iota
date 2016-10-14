/**
 * ADXR.java  v0.1  10 December 2014 2:04:09 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average Directional Movement Rating (ADXR)
 *
 * <p>Quantifies momentum change in the ADX.
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ADXR extends AbstractIndicator {

  public ADXR() {
    this(FOURTEEN);
  }

  public ADXR(final int period) {
    super(TA_LIB.adxrLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // The ADXR is equal to the current ADX plus the ADX from n bars ago divided by 2

    // TODO v0.2 implement DIY version
//    final double[] adx = new double[ohlcv.size() - TA_LIB.adxLookback(period)];
//    final RetCode outcome = TA_LIB.adx(start,
//                                       end,
//                                       ohlcv.highs(),
//                                       ohlcv.lows(),
//                                       ohlcv.closes(),
//                                       period,
//                                       outBegIdx,
//                                       outNBElement,
//                                       adx);
//    throwExceptionIfBad(outcome, ohlcv);
//
//    for (int i = ZERO, j = period - ONE; i < output.length; ++i) {
//      output[i] = (adx[i] + adx[j]) * HALF;
//    }

    return TA_LIB.adxr(start,
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
