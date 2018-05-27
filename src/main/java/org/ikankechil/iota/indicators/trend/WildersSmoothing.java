/**
 * WildersSmoothing.java  v0.1  23 May 2018 11:11:30 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Wilder's Smoothing by J. Welles Wilder
 *
 * <p>References:
 * <li>https://www.incrediblecharts.com/indicators/wilder_moving_average.php
 * <li>https://tulipindicators.org/wilders
 * <li>http://tlc.tdameritrade.com.sg/center/reference/Tech-Indicators/studies-library/V-Z/WildersSmoothing.html
 * <li>http://etfhq.com/blog/2010/08/19/wilders-smoothing/<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WildersSmoothing extends EMA {

  public WildersSmoothing(final int period) {
    super(period, (ONE / (double) period));
  }

}
