/**
 * ZeroLagMACD.java  v0.1  21 July 2015 12:08:46 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagMACD extends ZeroLagPriceOscillator {

  public ZeroLagMACD() {
    this(THIRTEEN, TWENTY_ONE, EIGHT);
  }

  public ZeroLagMACD(final int fast, final int slow, final int signal) {
    super(fast, slow, signal);
  }

  @Override
  double compute(final double fastZeroLagEMA, final double slowZeroLagEMA) {
    return fastZeroLagEMA - slowZeroLagEMA;
  }

}
