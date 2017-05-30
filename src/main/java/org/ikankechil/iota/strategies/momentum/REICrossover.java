/**
 * REICrossover.java  v0.1  30 May 2017 6:59:14 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.REI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * REI crossover by Thomas DeMark
 *
 * <p>Going above 60 and then dropping below 60 signals price weakness.<br>
 * Going below -60 and the rising above -60 signals price strength.<br>
 *
 * <p>https://www.forexstrategiesresources.com/forex-strategies-based-on-indicators/106-range-expansion-index-rei-trading-system/<br>
 * http://user42.tuxfamily.org/chart/manual/TD-Range-Expansion-Index.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class REICrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = -60.0;
  private static final double OVERBOUGHT = 60.0;

  public REICrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public REICrossover(final double buy, final double sell) {
    this(new REI(), buy, sell);
  }

  public REICrossover(final int period) {
    this(period, OVERSOLD, OVERBOUGHT);
  }

  public REICrossover(final int period, final double buy, final double sell) {
    this(new REI(period), buy, sell);
  }

  public REICrossover(final REI rei) {
    this(rei, OVERSOLD, OVERBOUGHT);
  }

  public REICrossover(final REI rei, final double buy, final double sell) {
    super(rei, buy, sell);
  }

}
