/**
 * LinearRegressionSlope.java  v0.2  20 December 2014 12:14:53 am
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Linear Regression Slope
 *
 *
 * <p>References:
 * <li>https://www.incrediblecharts.com/indicators/linear_regression_indicator.php<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class LinearRegressionSlope extends LinearRegression {

  public LinearRegressionSlope() {
    this(FOURTEEN);
  }

  public LinearRegressionSlope(final int period) {
    super(period);
  }

  @Override
  protected double linearRegression(final double sumX,
                                    final double sumY,
                                    final double sumXY,
                                    final double inverseDivisor) {
    return slope(sumX, sumY, sumXY, inverseDivisor);
  }

}
