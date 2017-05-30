/**
 * SlowRSICrossover.java  v0.1  30 May 2017 4:54:16 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.SlowRSI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * Slow RSI crossover by Vitali Apirine
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SlowRSICrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = 20.0;
  private static final double OVERBOUGHT = 80.0;

  public SlowRSICrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public SlowRSICrossover(final double buy, final double sell) {
    this(new SlowRSI(), buy, sell);
  }

  public SlowRSICrossover(final int period, final int ema) {
    this(period, ema, OVERSOLD, OVERBOUGHT);
  }

  public SlowRSICrossover(final int period, final int ema, final double buy, final double sell) {
    this(new SlowRSI(period, ema), buy, sell);
  }

  public SlowRSICrossover(final SlowRSI slowRSI) {
    this(slowRSI, OVERSOLD, OVERBOUGHT);
  }

  public SlowRSICrossover(final SlowRSI slowRSI, final double buy, final double sell) {
    super(slowRSI, buy, sell);
  }

}
