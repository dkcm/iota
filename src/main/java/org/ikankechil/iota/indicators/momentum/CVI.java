/**
 * CVI.java  v0.2  4 August 2015 9:59:15 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MedianPrice;
import org.ikankechil.iota.indicators.volatility.ATR;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chartmill Value Indicator (CVI) by Dirk Vandycke
 * <p>
 * http://edmond.mires.co/GES816/49-The%20Chartmill%20Value%20Indicator.pdf
 * http://edmond.mires.co/GES816/59-The%20Chartmill%20Value%20Indicator.pdf
 * http://www.chartmill.com/blog/wp-content/uploads/2014/04/TM201111R.pdf
 * http://www.chartmill.com/blog/wp-content/uploads/2014/04/TM201112R.pdf
 * http://www.chartmill.com/blog/wp-content/uploads/2014/04/TM201201R.pdf
 * http://www.trend-friends.be/sites/default/files/9/511VAND.pdf
 * http://traders.com/Documentation/FEEDbk_docs/2013/05/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CVI extends AbstractIndicator {

  private final ATR                atr;

  private static final MedianPrice MP = new MedianPrice();

  public CVI() {
    this(TEN);
  }

  public CVI(final int period) {
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
    // Value consensus, f = SMA((high + low) / 2, x)
    // volatility, v = SMA(TR, x)
    // Chartmill Value Indicator, CVI = adjusted close = (close - f) / v

    final double[] consensus = computeConsensus(ohlcv);
    final double[] volatility = computeVolatility(ohlcv);

    // compute indicator
    for (int i = ZERO, j = lookback, c = ONE; i < output.length; ++i, ++j, ++c) {
      output[i] = (ohlcv.close(j) - consensus[c]) / volatility[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected double[] computeConsensus(final OHLCVTimeSeries ohlcv) {
    final double[] medians = MP.generate(ohlcv).get(ZERO).values();
    return sma(medians, period); // value consensus
  }

  protected double[] computeVolatility(final OHLCVTimeSeries ohlcv) {
    return atr.generate(ohlcv).get(ZERO).values();
  }

}
