/**
 * NATR.java  v0.3  15 December 2014 11:57:02 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

/**
 * Normalised Average True Range (NATR) by John Forman
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C05/094FOR.pdf<br>
 * http://traders.com/Documentation/FEEDbk_docs/2006/05/TradersTips/TradersTips.html<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class NATR extends ATR {

  public NATR() {
    this(FOURTEEN);
  }

  public NATR(final int period) {
    super(period);
    
    // Formula:
    // NATR = atr / Close * 100
  }

  @Override
  double normalise(final double atr, final double close) {
    return atr / close * HUNDRED_PERCENT;
  }

}
