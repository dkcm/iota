/**
 * StandardDeviation.java  v0.2  15 December 2014 12:06:24 PM
 *
 * Copyright � 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Standard Deviation, square root of variance
 *
 * <p>http://mathworld.wolfram.com/StandardDeviation.html<br>
 * https://en.wikipedia.org/wiki/Standard_deviation<br>
 * http://www.lboro.ac.uk/media/wwwlboroacuk/content/mlsc/downloads/var_stand_deviat_ungroup.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StandardDeviation extends AbstractIndicator {

  private final double   stdDev;
  private final Variance variance;

  public StandardDeviation(final int period) {
    this(period, ONE);
  }

  public StandardDeviation(final int period, final double stdDev) {
    super(period, (period - ONE));
    throwExceptionIfNegative(stdDev);

    this.stdDev = stdDev;
    variance = new Variance(period);
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
    // Standard Deviation, sigma = sqrt(Variance)

    // compute variance
    variance.compute(start, end, values, outBegIdx, outNBElement, output);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = Math.sqrt(output[i]) * stdDev;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
