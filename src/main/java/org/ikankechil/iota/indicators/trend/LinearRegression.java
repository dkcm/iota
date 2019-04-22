/**
 * LinearRegression.java  v0.4  10 December 2014 10:24:29 AM
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Linear Regression
 *
 * <p>References:
 * <li>https://www.incrediblecharts.com/indicators/linear_regression_indicator.php
 * <li>http://etfhq.com/blog/2010/11/06/linear-regression-indicator-time-series-forecast/<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class LinearRegression extends AbstractIndicator {

  private final double inversePeriod;

  public LinearRegression() {
    this(FOURTEEN);
  }

  public LinearRegression(final int period) {
    super(period, period - ONE);

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
    // y = mx + c
    // m = (n x sum(xy) - sum(x) x sum(y)) / (n x sum(x^2) - (sum(x))^2)
    // c = (sum(y) - sum(x)) / n
    // n = period

    final double sumX = period * lookback * HALF;
    final double sumX2 = sumX * (TWO_THIRDS * period - THIRD);
    final double inverseDivisor = ONE / ((sumX * sumX) - (period * sumX2));

    double sumY = sum(values, ZERO, lookback);
    double first = ZERO;

    for (int i = ZERO; i < output.length; ++i) {
      // compute sumXY
      double sumXY = ZERO;
      for (int v = i, x = lookback; v < i + period; ++v, --x) {
        final double y = values[v];
        sumXY += x * y;
      }
      // compute sumY
      sumY += (values[i + lookback] - first);
      first = values[i];

      output[i] = linearRegression(sumX, sumY, sumXY, inverseDivisor);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected double linearRegression(final double sumX,
                                    final double sumY,
                                    final double sumXY,
                                    final double inverseDivisor) {
    final double m = slope(sumX, sumY, sumXY, inverseDivisor);
    final double c = intercept(sumX, sumY, m);
    return straightLine(m, c);
  }

  protected double slope(final double sumX,
                         final double sumY,
                         final double sumXY,
                         final double inverseDivisor) {
    return (period * sumXY - sumX * sumY) * inverseDivisor;
  }

  protected double intercept(final double sumX,
                             final double sumY,
                             final double m) {
    return (sumY - m * sumX) * inversePeriod;
  }

  protected double straightLine(final double m, final double c) {
    return m * lookback + c;
  }

}
