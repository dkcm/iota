/**
 * OBVDI.java  v0.1 17 July 2015 2:45:29 PM
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
 * On-balance Volume Disparity Indicator (OBVDI) by Phillip C. Holt
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C06/ENHANCI.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OBVDI extends AbstractIndicator {
  // The rules for the OBVDI system are:
  // 1. Buy: when the four-week simple moving average of OBVDI rises through the 0.85 or 0.95 level.
  // 2. Sell: when the moving average falls through the 0.95 level.

  private final BollingerB bollingerB;     // Bollinger %b

  private static final OBV OBV = new OBV();

  public OBVDI() {
    this(THIRTY_THREE);
  }

  public OBVDI(final int period) {
    this(period, TWO);
  }

  public OBVDI(final int period, final double stdDev) {
    this(period, stdDev, stdDev);
  }

  public OBVDI(final int period, final double stdDevUpper, final double stdDevLower) {
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
    // %B of on-balance volume:
    // 1+((OBV()-(mov(OBV(),33,s)-(2*(std(OBV(),33)))))/(mov(OBV(),33,s)+(2*(std(OBV(),33)))-(mov(OBV(),33,s)-(2*(std(OBV(),33))))))
    //
    // on-balance volume disparity indicator:
    // %B of price / %B of on-balance volume

    // Bollinger %b of closing price
    final double[] bollingerBPrice = bollingerB.generate(ohlcv).get(ZERO).values();

    // Bollinger %b of OBV
    final TimeSeries obv = OBV.generate(ohlcv).get(ZERO);  // OBV lookback == 0
    final double[] bollingerBOBV = bollingerB.generate(obv).get(ZERO).values();

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = (ONE + bollingerBPrice[i]) / (ONE + bollingerBOBV[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
