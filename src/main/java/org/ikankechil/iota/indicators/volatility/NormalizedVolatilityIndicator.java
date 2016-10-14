/**
 * NormalizedVolatilityIndicator.java  v0.2  12 August 2015 1:10:10 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Normalized Volatility Indicator by Rajesh Kayakkal
 *
 * <p>http://edmond.mires.co/GES816/21-Normalized%20Volatility%20Indicator.pdf<br>
 * http://traders.com/Documentation/FEEDbk_docs/2010/08/TradersTips.html<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class NormalizedVolatilityIndicator extends AbstractIndicator {

  private final ATR        atr;

  private static final int AVE_DAYS_IN_QTR = SIXTY_FOUR;

  public NormalizedVolatilityIndicator() {
    this(AVE_DAYS_IN_QTR); // average number of trading days in a quarter
  }

  public NormalizedVolatilityIndicator(final int period) {
    super(period, period);

    atr = new ATR(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // NormalizedVolatilityIndicator = 64-Day average true range / End-of-day price * 100

    // compute ATR
    final TimeSeries atrs = atr.generate(ohlcv).get(ZERO);

    // compute indicator
    for (int i = ZERO, c = i + lookback; i < output.length; ++i, ++c) {
      output[i] = atrs.value(i) / ohlcv.close(c) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
