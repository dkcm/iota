/**
 * DEMA.java  v0.3  9 December 2014 12:10:01 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Double Exponential Moving Average (DEMA) by Patrick Mulloy
 *
 * <p>www.investopedia.com/articles/trading/10/double-exponential-moving-average.asp<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V12/C01/SMOOTHI.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class DEMA extends AbstractIndicator {

  private final EMA ema;

  public DEMA(final int period) {
    super(period, (period - ONE) << ONE);

    ema = new EMA(period);
  }

  public DEMA(final EMA ema) {
    super(ema.lookback() + ONE, ema.lookback() << ONE);

    this.ema = ema;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // DEMA = 2EMA – EMA(EMA)

    // compute EMAs
    final int emaLookback = ema.lookback();
    final double[] ema1 = new double[values.length - emaLookback];
    ema.compute(ZERO, values.length, values, outBegIdx, outNBElement, ema1);
    final double[] ema2 = new double[ema1.length - emaLookback];
    ema.compute(ZERO, ema1.length, ema1, outBegIdx, outNBElement, ema2);

    // compute indicator
    for (int i = ZERO, j = i + emaLookback; i < output.length; ++i, ++j) {
      output[i] = (TWO * ema1[j]) - ema2[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
