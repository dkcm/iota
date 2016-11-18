/**
 * StandardError.java  v0.1  18 November 2016 5:23:52 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Standard Error, standard deviation divided by square root of sample size
 *
 * <p>http://mathworld.wolfram.com/StandardError.html<br>
 * http://web.eecs.umich.edu/~fessler/papers/files/tr/stderr.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StandardError extends AbstractIndicator {

  private final double   stdErr;
  private final Variance variance;
  private final double   inversePeriod;

  public StandardError(final int period) {
    this(period, ONE);
  }

  public StandardError(final int period, final double stdErr) {
    super(period, (period - ONE));
    throwExceptionIfNegative(stdErr);

    this.stdErr = stdErr;
    variance = new Variance(period);
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
    // Standard Deviation, sigma = sqrt(Variance)
    // Standard Error = Standard Deviation / sqrt(n) = sqrt(Variance / n)

    // compute variance
    variance.compute(start, end, values, outBegIdx, outNBElement, output);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = Math.sqrt(output[i] * inversePeriod) * stdErr;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
