/**
 * NormalizedVolatilityIndicator.java  v0.1 12 August 2015 1:10:10 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Normalized Volatility Indicator by Rajesh Kayakkal
 * <p>
 * http://edmond.mires.co/GES816/21-Normalized%20Volatility%20Indicator.pdf
 * http://traders.com/Documentation/FEEDbk_docs/2010/08/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NormalizedVolatilityIndicator extends AbstractIndicator {

  private static final int AVE_DAYS_IN_QTR = SIXTY_FOUR;

  public NormalizedVolatilityIndicator() {
    this(AVE_DAYS_IN_QTR); // average number of trading days in a quarter
  }

  public NormalizedVolatilityIndicator(final int period) {
    super(period, TA_LIB.atrLookback(period));
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
    final double[] closes = ohlcv.closes();
    final double[] atr = new double[ohlcv.size() - lookback];
    final RetCode outcome = TA_LIB.atr(start,
                                       end,
                                       ohlcv.highs(),
                                       ohlcv.lows(),
                                       closes,
                                       period,
                                       outBegIdx,
                                       outNBElement,
                                       atr);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO, c = i + lookback; i < output.length; ++i, ++c) {
      output[i] = atr[i] / closes[c] * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return outcome;
  }

}
