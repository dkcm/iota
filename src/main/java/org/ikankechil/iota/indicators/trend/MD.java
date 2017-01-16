/**
 * MD.java  v0.1  15 January 2017 11:03:54 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * McGinley Dynamic (MD)
 *
 * <p>http://www.investopedia.com/articles/forex/09/mcginley-dynamic-indicator.asp<br>
 * http://www.investopedia.com/ask/answers/121614/what-mcginley-dynamic-indicator-formula-and-how-it-calculated.asp<br>
 * http://www.forexfactory.com/showthread.php?t=204858<br>
 * https://www.tradingview.com/script/dY0K4oJm-McGinley-Dynamic-Average/<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MD extends AbstractIndicator {

  private final double inverseN;

  public MD() {
    this(TEN);
  }

  public MD(final int n) {
    super(ZERO);
    throwExceptionIfNegative(n);

    inverseN = ONE / (double) n;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // MD = MD-1 + (price – MD-1) / (N * (price / MD-1)^4)

    int i = ZERO;
    double md = values[i];
    output[i] = md;

    while (++i < output.length) {
      final double value = values[i];
      output[i] = md += (value - md) * inverseN * Math.pow(value / md, -FOUR);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
