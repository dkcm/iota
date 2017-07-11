/**
 * REMA.java  v0.1  3 May 2017 11:41:00 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Regularised Exponential Moving Average (REMA) by Chris Satchwell
 *
 * <p>http://traders.com/documentation/feedbk_docs/2003/07/TradersTips/TradersTips.html<br>
 * http://fxcodebase.com/code/viewtopic.php?f=38&t=59636<br>
 * https://user42.tuxfamily.org/chart/manual/Regularized-Exponential-Moving-Average.html<br>
 * http://karthikmarar.blogspot.sg/2013/05/regularized-indicators.html<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class REMA extends AbstractIndicator implements MA {

  private final double alpha;
  private final double alpha_;
  private final double lambda1;
  private final double lambda2;

  public REMA(final int period) {
    this(period, HALF);
  }

  public REMA(final int period, final double lambda) {
    super(period, period - ONE);

    alpha = TWO / (double) (period + ONE);
    final double inverseOnePlusLambda = ONE / (lambda + ONE);

    alpha_ = alpha * inverseOnePlusLambda;
    lambda1 = (ONE - alpha + TWO * lambda) * inverseOnePlusLambda;
    lambda2 = -lambda * inverseOnePlusLambda;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // REMA[i] = (REMA[i-1]*(1+2*Lambda)+Alpha*(Price[i]-REMA[i-1])-Lambda*REMA[i-2])/(1+Lambda)
    // where
    // Alpha=2/(Length+1)
    // Lambda=0.5

    // first value is SMA
    int v = ZERO;
    double rema2 = values[v];
    while (++v < period) {
      rema2 += values[v];
    }
    int i = ZERO;
    output[i] = rema2 /= period;

    // second value is EMA
    double rema1 = output[++i] = rema2 + alpha * (values[v] - rema2);

    // subsequent values are REMA
    while (++i < output.length) {
      output[i] = (alpha_ * values[++v]) + (lambda1 * rema1) + (lambda2 * rema2);

      // shift forward
      rema2 = rema1;
      rema1 = output[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
