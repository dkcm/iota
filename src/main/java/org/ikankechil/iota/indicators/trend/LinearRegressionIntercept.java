/**
 * LinearRegressionIntercept.java  v0.2  5 January 2015 9:58:35 AM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Linear Regression Intercept
 *
 *
 * <p>References:
 * <li>https://www.incrediblecharts.com/indicators/linear_regression_indicator.php<br>
 *
 * @author Daniel Kuan
 * @version 0.7
 */
public class LinearRegressionIntercept extends LinearRegression {

  public LinearRegressionIntercept() {
    this(FOURTEEN);
  }

  public LinearRegressionIntercept(final int period) {
    super(period);
  }

  @Override
  protected double linearRegression(final double sumX,
                                    final double sumY,
                                    final double sumXY,
                                    final double inverseDivisor) {
    final double m = slope(sumX, sumY, sumXY, inverseDivisor);
    return intercept(sumX, sumY, m);
  }

}
