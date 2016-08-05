/**
 * ZeroLagPPO.java  v0.1  21 July 2015 1:31:08 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagPPO extends ZeroLagPriceOscillator {

  public ZeroLagPPO() {
    this(THIRTEEN, TWENTY_ONE, EIGHT);
  }

  public ZeroLagPPO(final int fast, final int slow, final int signal) {
    super(fast, slow, signal);
  }

  @Override
  double compute(final double fastZeroLagEMA, final double slowZeroLagEMA) {
    return (fastZeroLagEMA / slowZeroLagEMA - ONE) * HUNDRED_PERCENT;
  }

}
