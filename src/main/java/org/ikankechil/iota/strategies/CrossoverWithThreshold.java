/**
 * CrossoverWithThreshold.java  v0.2  30 July 2015 3:56:25 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.indicators.Indicator;

/**
 * Crossover with threshold strategy.
 * <p>
 * Cross overs / unders must be below / above a threshold.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CrossoverWithThreshold extends Crossover {

  private final double buy;
  private final double sell;

  public CrossoverWithThreshold(final Indicator indicator, final double threshold) {
    this(indicator, -threshold, threshold);
  }

  public CrossoverWithThreshold(final Indicator indicator, final double buy, final double sell) {
    super(indicator);

    this.buy = buy;
    this.sell = sell;
  }

  public CrossoverWithThreshold(final Indicator indicator, final int fastIndex, final int slowIndex, final double threshold) {
    this(indicator, fastIndex, slowIndex, -threshold, threshold);
  }

  public CrossoverWithThreshold(final Indicator indicator, final int fastIndex, final int slowIndex, final double buy, final double sell) {
    super(indicator, fastIndex, slowIndex);

    this.buy = buy;
    this.sell = sell;
  }

  public CrossoverWithThreshold(final Indicator fast, final Indicator slow, final double threshold) {
    this(fast, slow, -threshold, threshold);
  }

  public CrossoverWithThreshold(final Indicator fast, final Indicator slow, final double buy, final double sell) {
    super(fast, slow);

    this.buy = buy;
    this.sell = sell;
  }

  @Override
  protected boolean buy(final double... doubles) {
    // fast indicator after crossover is below threshold
    final double fastToday = doubles[TWO];
    return (fastToday < buy) && super.buy(doubles);
  }

  @Override
  protected boolean sell(final double... doubles) {
    // fast indicator after crossunder is above threshold
    final double fastToday = doubles[TWO];
    return (fastToday > sell) && super.sell(doubles);
  }

}
