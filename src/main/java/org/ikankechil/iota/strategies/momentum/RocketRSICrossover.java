/**
 * RocketRSICrossover.java  v0.1  3 May 2018 10:04:45 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.RocketRSI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * Rocket RSI crossover by John Ehlers
 *
 * <p>Buy when indicator crosses over -2.00<br>
 * Sell when indicator crosses below 2.00<br>
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2018/05/TradersTips.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RocketRSICrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = -2.0;
  private static final double OVERBOUGHT = 2.0;

  public RocketRSICrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public RocketRSICrossover(final double buy, final double sell) {
    this(new RocketRSI(), buy, sell);
  }

  public RocketRSICrossover(final int smooth, final int rsi) {
    this(new RocketRSI(smooth, rsi));
  }

  public RocketRSICrossover(final int smooth, final int rsi, final double buy, final double sell) {
    this(new RocketRSI(smooth, rsi), buy, sell);
  }

  public RocketRSICrossover(final RocketRSI rocketRSI) {
    this(rocketRSI, OVERSOLD, OVERBOUGHT);
  }

  public RocketRSICrossover(final RocketRSI rocketRSI, final double buy, final double sell) {
    super(rocketRSI, buy, sell);
  }

}
