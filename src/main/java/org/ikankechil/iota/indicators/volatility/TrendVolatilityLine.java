/**
 * TrendVolatilityLine.java v0.1 20 October 2015 6:53:47 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.GMMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Trend Volatility Line (TVL) by Darryl Guppy
 * <p>
 * http://www.omnitrader.com/PDFs/GMMA2.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class TrendVolatilityLine extends AbstractIndicator {

  private static final GMMA GMMA = new GMMA();

  public TrendVolatilityLine() {
    super(GMMA.lookback());
  }

  public static void main(final String[] args) {
    // TODO Auto-generated method stub
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Algorithm
    // 1. 1st stop-loss line projected to the right from lookback high (or GMMA Long1 EMA?)
    // 2. Form new stop-loss line at GMMA Long1 EMA when GMMA Long6 EMA crosses
    //    existing stop-loss line
    // 3. Exit when close below stop-loss line
    // TODO algo for downtrend

//    stopLoss = long1
//        isUptrend = long1 >= long6;
//        if uptrend
//          long6 > stopLoss
//        else
//          long6 < stopLoss

    final List<TimeSeries> gmma = GMMA.generate(ohlcv);
    final int size = gmma.size();
    final TimeSeries long6 = gmma.get(size - ONE);
    final TimeSeries long1 = gmma.get(size / TWO);

    double stopLoss = ohlcv.close(lookback);
    int i = ZERO;
    final boolean uptrend = (long1.value(i) >= long6.value(i));

    // compute indicator
    for (; i < output.length; ++i) {
      if (long6.value(i) >= stopLoss) {
        stopLoss = long1.value(i);
      }
      output[i] = stopLoss;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
