/**
 * TimeSeriesForecast.java  v0.2  8 December 2014 6:25:21 PM
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Time Series Forecast (TSF)
 *
 * <p>References:
 * <li>http://etfhq.com/blog/2010/11/06/linear-regression-indicator-time-series-forecast/
 * <li>https://library.tradingtechnologies.com/trade/chrt-ti-time-series-forecast.html
 * <li>https://www.metastock.com/customer/resources/taaz/?p=109<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TimeSeriesForecast extends LinearRegression {

  public TimeSeriesForecast() {
    this(FOURTEEN);
  }

  public TimeSeriesForecast(final int period) {
    super(period);
  }

  @Override
  protected double straightLine(final double m, final double c) {
    return m * period + c;
  }

}
