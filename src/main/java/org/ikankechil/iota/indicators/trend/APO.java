/**
 * APO.java  v0.2  19 December 2014 5:57:17 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Absolute Price Oscillator (APO)
 *
 * <p>https://www.fidelity.com/learning-center/trading-investing/technical-analysis/technical-indicator-guide/apo<br>
 * https://www.tradingtechnologies.com/help/x-study/technical-indicator-definitions/absolute-price-oscillator-apo/<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class APO extends IndicatorWithSignalLine {

  private final int fast;
  private final int slow;

  public APO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public APO(final int fast, final int slow, final int signal) {
    super(signal, Math.max(fast, slow) + signal - TWO);
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Absolute Price Oscillator (APO): (12-day EMA - 26-day EMA)
    // Signal Line: 9-day EMA of APO

    // compute fast and slow EMAs
    final double[] fastEMAs = ema(values, fast);
    final double[] slowEMAs = ema(values, slow);

    // compute indicator
    for (int i = ZERO, j = slow - fast; i < output.length; ++i, ++j) {
      output[i] = fastEMAs[j] - slowEMAs[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
