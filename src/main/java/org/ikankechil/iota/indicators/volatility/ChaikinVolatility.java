/**
 * ChaikinVolatility.java v0.1 7 January 2015 7:17:18 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chaikin's Volatility
 * <p>
 * http://www.fmlabs.com/reference/default.htm?url=ChaikinVolatility.htm
 * http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=120
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChaikinVolatility extends AbstractIndicator {

  public ChaikinVolatility() {
    this(TEN);
  }

  public ChaikinVolatility(final int period) {
    super(period, TA_LIB.emaLookback(period) + period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // EMAHL = n-period exponential moving average of (high - low)
    // ChaikinVolatility = ((EMAHL / EMAHL n-periods ago) - 1) * 100

    // compute ranges
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] ranges = new double[ohlcv.size()];
    for (int i = ZERO; i < ranges.length; ++i) {
      ranges[i] = highs[i] - lows[i];
    }

    // compute EMA of ranges
    final double[] ema = new double[ranges.length - (lookback - period)];
    final RetCode outcome = TA_LIB.ema(ZERO,
                                       ranges.length - ONE,
                                       ranges,
                                       period,
                                       outBegIdx,
                                       outNBElement,
                                       ema);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO, j = i + period; i < output.length; ++i, ++j) {
      output[i] = ((ema[j] / ema[i]) - ONE) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
