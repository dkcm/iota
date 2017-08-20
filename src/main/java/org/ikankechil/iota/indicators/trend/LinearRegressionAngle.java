/**
 * LinearRegressionAngle.java  v0.2  20 December 2014 12:17:57 am
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Linear Regression Angle
 *
 *
 * <p>References:
 * <li>https://www.incrediblecharts.com/indicators/linear_regression_indicator.php<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class LinearRegressionAngle extends LinearRegression {

  private static final double RADIAN = 180 / Math.PI;

  public LinearRegressionAngle() {
    this(FOURTEEN);
  }

  public LinearRegressionAngle(final int period) {
    super(period);
  }

  @Override
  protected double linearRegression(final double sumX,
                                    final double sumY,
                                    final double sumXY,
                                    final double inverseDivisor) {
    final double m = slope(sumX, sumY, sumXY, inverseDivisor);
    return Math.atan(m) * RADIAN;
  }

}
