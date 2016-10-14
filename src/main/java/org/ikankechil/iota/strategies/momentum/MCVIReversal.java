/**
 * MCVIReversal.java  v0.2  16 September 2016 12:17:58 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.MCVI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 *
 *
 * <p>https://traderedge.net/2013/01/19/modified-chartmill-value-indicator-mcvi/<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MCVIReversal extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = -0.51;
  private static final double OVERBOUGHT = 0.43;

  public MCVIReversal() {
    this(THREE);
  }

  public MCVIReversal(final int period) {
    this(period, OVERSOLD, OVERBOUGHT);
  }

  public MCVIReversal(final int period, final double oversold, final double overbought) {
    super(new MCVI(period), oversold, overbought);
  }

}
