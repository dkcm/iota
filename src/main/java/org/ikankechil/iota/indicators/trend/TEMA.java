/**
 * TEMA.java  v0.2  7 January 2015 10:41:58 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Triple Exponential Moving Average (TEMA) by Patrick Mulloy
 *
 * <p>http://etfhq.com/blog/2010/11/17/double-and-triple-exponential-moving-average/<br>
 * http://www.forextraders.com/forex-analysis/forex-technical-analysis/triple-exponential-moving-average-the-tema-indicator.html<br>
 * https://www.tradingtechnologies.com/help/x-study/technical-indicator-definitions/triple-exponential-moving-average-tema/<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V12/C01/SMOOTHI.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TEMA extends AbstractIndicator {

  private final EMA ema;

  public TEMA(final int period) {
    this(new EMA(period));
  }

  public TEMA(final EMA ema) {
    super(ema.lookback() + ONE, ema.lookback() * THREE);

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
    // TEMA = 3EMA - 3EMA(EMA) + EMA(EMA(EMA))

    // compute EMAs
    final int emaLookback = ema.lookback();
    final double[] ema1 = new double[values.length - emaLookback];
    ema.compute(ZERO, values.length, values, outBegIdx, outNBElement, ema1);
    final double[] ema2 = new double[ema1.length - emaLookback];
    ema.compute(ZERO, ema1.length, ema1, outBegIdx, outNBElement, ema2);
    final int size = output.length;
    ema.compute(ZERO, size, ema2, outBegIdx, outNBElement, output); // re-use

    // compute indicator
    for (int i = ZERO, j = i + emaLookback, k = j + emaLookback;
         i < size;
         ++i, ++j, ++k) {
      output[i] += THREE * (ema1[k] - ema2[j]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
