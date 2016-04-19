/**
 * CVI.java	v0.1	4 August 2015 9:59:15 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chartmill Value Indicator (CVI) by Dirk Vandycke
 * <p>
 * http://edmond.mires.co/GES816/59-The%20Chartmill%20Value%20Indicator.pdf
 * http://chartmill.com/blog/wp-content/uploads/2014/04/TM201111R.pdf
 * http://www.trend-friends.be/sites/default/files/9/511VAND.pdf
 * http://traders.com/Documentation/FEEDbk_docs/2013/05/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CVI extends AbstractIndicator {

  public CVI() {
    this(TEN);
  }

  public CVI(final int period) {
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
    // Value consensus, f = SMA((high + low) / 2, x)
    // volatility, v = SMA(TR, x)
    // Chartmill Value Indicator, CVI = adjusted close = (close - f) / v

    final double[] consensus = computeConsensus(ohlcv);
    final double[] volatility = computeVolatility(ohlcv);

    // compute indicator
    final double[] closes = ohlcv.closes();
    for (int i = ZERO, j = lookback, c = ONE; i < output.length; ++i, ++j, ++c) {
      output[i] = (closes[j] - consensus[c]) / volatility[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected double[] computeConsensus(final OHLCVTimeSeries ohlcv) {
    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final int size = ohlcv.size();
    final double[] medians = new double[size];
    final RetCode outcome = TA_LIB.medPrice(ZERO,
                                            size - ONE,
                                            ohlcv.highs(),
                                            ohlcv.lows(),
                                            outBegIdx,
                                            outNBElement,
                                            medians);
    throwExceptionIfBad(outcome, ohlcv);

    return sma(medians, period); // value consensus
  }

  protected double[] computeVolatility(final OHLCVTimeSeries ohlcv) {
    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final int size = ohlcv.size();
    final double[] volatility = new double[size - lookback];
    final RetCode outcome = TA_LIB.atr(ZERO,
                                       size - ONE,
                                       ohlcv.highs(),
                                       ohlcv.lows(),
                                       ohlcv.closes(),
                                       period,
                                       outBegIdx,
                                       outNBElement,
                                       volatility);
    throwExceptionIfBad(outcome, ohlcv);

    return volatility;
  }

}
