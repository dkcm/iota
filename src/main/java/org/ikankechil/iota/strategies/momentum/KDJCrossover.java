/**
 * KDJCrossover.java  v0.1  30 May 2017 6:22:46 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.KDJ;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * KDJ crossover
 *
 * <p>Buy: when %J < 0, with %K and %D at the bottom range<br>
 * Sell: when %J > 100, with %K and %D at the top range<br>
 *
 * <p>"A negative value of %J with %K and %D at the bottom range indicates a
 * strong over sold condition. When the %J value goes above 100, with %K and %D
 * at the top range, indicates a strong over bought condition."<br>
 *
 * <p>http://www.wisestocktrader.com/kdjs/320-stochastic-j-kdj
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KDJCrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = 0.0;
  private static final double OVERBOUGHT = 100.0;

  public KDJCrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public KDJCrossover(final double buy, final double sell) {
    this(new KDJ(), buy, sell);
  }

  public KDJCrossover(final KDJ kdj) {
    this(kdj, OVERSOLD, OVERBOUGHT);
  }

  public KDJCrossover(final KDJ kdj, final double buy, final double sell) {
    super(kdj, buy, sell);
  }

}
