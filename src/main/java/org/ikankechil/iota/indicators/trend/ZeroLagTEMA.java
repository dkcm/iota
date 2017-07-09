/**
 * ZeroLagTEMA.java  v0.2  27 September 2016 7:38:26 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Zero-Lag Triple Exponential Moving Average (ZLTEMA)
 *
 * <p>References:
 * <li>http://forum.actfx.com/Topic4156.aspx<br>
 * <li>http://fxcodebase.com/code/viewtopic.php?f=38&t=61383<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ZeroLagTEMA extends ZeroLagMA {

  public ZeroLagTEMA() {
    this(TEN);
  }

  public ZeroLagTEMA(final int period) {
    this(new TEMA(period));
  }

  public ZeroLagTEMA(final TEMA tema) {
    super(tema);

    // Formula:
    // This indicator is based on method of data smoothing developed by Patrick
    // Mulloy (February 1994, Stocks & Commodities) and John Ehlers (March
    // 2000), allowing to avoid the lags in moving averages.
    //
    // TMA1:= TEMA(CLOSE, period);
    // TMA2:= TEMA(TMA1, period);
    // ZeroLag TEMA:= TMA1 + (TMA1 - TMA2);
  }

}
