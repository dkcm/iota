/**
 * ZeroLagDEMA.java  0.1  8 July 2017 3:47:12 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 * Zero-Lag Double Exponential Moving Average (ZLDEMA)
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagDEMA extends ZeroLagMA {

  public ZeroLagDEMA() {
    this(TEN);
  }

  public ZeroLagDEMA(final int period) {
    this(new DEMA(period));
  }

  public ZeroLagDEMA(final DEMA dema) {
    super(dema);
  }

}
