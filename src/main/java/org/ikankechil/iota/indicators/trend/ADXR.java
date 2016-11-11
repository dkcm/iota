/**
 * ADXR.java  v0.2  10 December 2014 2:04:09 am
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
 * <p>https://www.linnsoft.com/techind/adxr-avg-directional-movement-rating<br>
 * https://www.scottrade.com/knowledge-center/investment-education/research-analysis/technical-analysis/the-indicators/average-directional-movement-index-rating-adxr.html<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ADXR extends AbstractIndicator {

  private final ADX adx;

  public ADXR() {
    this(FOURTEEN);
  }

  public ADXR(final int period) {
    super(period, (period * THREE) - TWO);

    adx = new ADX(period);
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

    // compute ADX
    final double[] adxs = adx.generate(ohlcv).get(ZERO).values();

    // compute indicator
    for (int i = ZERO, j = period - ONE; i < output.length; ++i, ++j) {
      output[i] = (adxs[i] + adxs[j]) * HALF;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
