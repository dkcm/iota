/**
 * CrossoverWithThreshold.java  v0.1  30 July 2015 3:56:25 PM
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
 * @version 0.1
 */
public class CrossoverWithThreshold extends Crossover {

  private final double low;
  private final double high;

  public CrossoverWithThreshold(final Indicator indicator, final double threshold) {
    this(indicator, -threshold, threshold);
  }

  public CrossoverWithThreshold(final Indicator indicator, final double low, final double high) {
    super(indicator);

    this.low = low;
    this.high = high;
  }

  public CrossoverWithThreshold(final Indicator fast, final Indicator slow, final double threshold) {
    this(fast, slow, -threshold, threshold);
  }

  public CrossoverWithThreshold(final Indicator fast, final Indicator slow, final double low, final double high) {
    super(fast, slow);

    this.low = low;
    this.high = high;
  }

  @Override
  protected boolean buy(final double... doubles) {
    final double fastYesterday = doubles[ZERO];
    final double slowYesterday = doubles[ONE];
    final double fastToday = doubles[TWO];
    final double slowToday = doubles[THREE];

    return (fastToday < low) &&
           super.buy(fastYesterday, slowYesterday, fastToday, slowToday);
  }

  @Override
  protected boolean sell(final double... doubles) {
    final double fastYesterday = doubles[ZERO];
    final double slowYesterday = doubles[ONE];
    final double fastToday = doubles[TWO];
    final double slowToday = doubles[THREE];

    return (fastToday > high) &&
           super.sell(fastYesterday, slowYesterday, fastToday, slowToday);
  }

}
