/**
 * ChaikinVolatility.java  v0.2 7 January 2015 7:17:18 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Range;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chaikin's Volatility
 * <p>
 * http://www.fmlabs.com/reference/default.htm?url=ChaikinVolatility.htm
 * http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=120
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ChaikinVolatility extends AbstractIndicator {

  private static final Range RANGE = new Range();

  public ChaikinVolatility() {
    this(TEN);
  }

  public ChaikinVolatility(final int period) {
    super(period, (period << ONE) - ONE);
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

    // compute EMA of ranges
    final double[] ranges = RANGE.generate(ohlcv, start).get(ZERO).values();
    final double[] ema = ema(ranges, period);

    // compute indicator
    for (int i = ZERO, j = i + period; i < output.length; ++i, ++j) {
      output[i] = ((ema[j] / ema[i]) - ONE) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
