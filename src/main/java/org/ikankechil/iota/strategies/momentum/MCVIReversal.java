/**
 * MCVIReversal.java  v0.3  16 September 2016 12:17:58 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.MCVI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * MCVI Reversal by Brian Johnson
 *
 * <p>Buy:
 * 1. MCVI crosses under -0.51
 * Sell:
 * 1. MCVI crosses above +0.43
 *
 * <p>https://traderedge.net/2013/01/19/modified-chartmill-value-indicator-mcvi/<br>
 *
 * @author Daniel Kuan
 * @version 0.3
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

  public MCVIReversal(final double oversold, final double overbought) {
    this(THREE, oversold, overbought);
  }

  public MCVIReversal(final int period, final double oversold, final double overbought) {
    this(new MCVI(period), oversold, overbought);
  }

  public MCVIReversal(final MCVI mcvi) {
    this(mcvi, OVERSOLD, OVERBOUGHT);
  }

  public MCVIReversal(final MCVI mcvi, final double oversold, final double overbought) {
    super(mcvi, oversold, overbought);
  }

}
