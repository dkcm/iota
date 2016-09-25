/**
 * NVDI.java  v0.1  17 July 2015 2:46:42 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.momentum.BollingerB;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Negative Volume Disparity Indicator (NVDI) by Phillip C. Holt
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C06/ENHANCI.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class NVDI extends AbstractIndicator {

  private final BollingerB bollingerB;     // Bollinger %b

  private static final NVI NVI = new NVI();

  public NVDI() {
    this(THIRTY_THREE);
  }

  public NVDI(final int period) {
    this(period, TWO);
  }

  public NVDI(final int period, final double stdDev) {
    this(period, stdDev, stdDev);
  }

  public NVDI(final int period, final double stdDevUpper, final double stdDevLower) {
    super(period, period - ONE);

    bollingerB = new BollingerB(period, stdDevUpper, stdDevLower);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // %B of price:
    // 1+((c-(mov(c,33,s)-(2*(std(c,33)))))/(mov(c,33,s) +(2*(std(c,33)))-(mov(c,33,s)-(2*(std(c,33))))))
    //
    // %B of negative volume:
    // 1+((NVI()-(mov(NVI(),33,s)-(2*(std(NVI(),33)))))/(mov(NVI(),33,s)+(2*(std(NVI(),33)))-(mov(NVI(),33,s)-(2*(std(NVI(),33))))))
    //
    // negative volume disparity indicator:
    // %B of price / %B of negative volume

    // Bollinger %b of closing price
    final TimeSeries bollingerBPrice = bollingerB.generate(ohlcv).get(ZERO);

    // Bollinger %b of NVI
    final TimeSeries obv = NVI.generate(ohlcv).get(ZERO);
    final TimeSeries bollingerBNVI = bollingerB.generate(obv).get(ZERO);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = (ONE + bollingerBPrice.value(i)) / (ONE + bollingerBNVI.value(i));
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
