/**
 * Variance.java  v0.2  9 December 2014 12:52:05 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Variance
 *
 * <p>http://mathworld.wolfram.com/Variance.html<br>
 * https://en.wikipedia.org/wiki/Variance<br>
 * http://www.lboro.ac.uk/media/wwwlboroacuk/content/mlsc/downloads/var_stand_deviat_ungroup.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class Variance extends AbstractIndicator {

  private final double inversePeriod;

  public Variance(final int period) {
    super(period, (period - ONE));

    inversePeriod = ONE / (double) period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Variance, sigma^2 = (sum(x^2) / n) - mean(x)^2

    // compute sum(x) and sum(x^2)
    int v = ZERO;
    double value = values[v];
    double sumValue = value;
    double sumValueSquared = value * value;
    while (++v < period) {
      value = values[v];
      sumValue += value;
      sumValueSquared += (value * value);
    }

    // compute indicator
    int i = ZERO;
    output[i] = variance(sumValue, sumValueSquared);
    double firstValue = values[i];

    for (double deltaValue = ZERO; ++i < output.length; ++v) {
      value = values[v];
      sumValue += deltaValue = (value - firstValue);
      sumValueSquared += (value + firstValue) * deltaValue; // a^2 - b^2 = (a + b)(a - b)
//      sumValueSquared += (value * value) - (firstValue * firstValue);
      output[i] = variance(sumValue, sumValueSquared);

      // shift forward in time
      firstValue = values[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final double variance(final double sumValue, final double sumValueSquared) {
    final double meanValue = sumValue * inversePeriod;
    return (sumValueSquared * inversePeriod) - (meanValue * meanValue);
  }

}
