/**
 * TrendlineBreakout.java  v0.1  22 September 2016 9:33:52 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import org.ikankechil.iota.indicators.HighPrice;
import org.ikankechil.iota.indicators.LowPrice;
import org.ikankechil.iota.indicators.pattern.Trendlines;
import org.ikankechil.iota.strategies.CompositeStrategy;
import org.ikankechil.iota.strategies.Crossover;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendlineBreakout extends CompositeStrategy {

  private static final HighPrice HIGH = new HighPrice();
  private static final LowPrice  LOW  = new LowPrice();

  private static final int       ZERO = 0;
  private static final int       ONE  = 1;

  public TrendlineBreakout(final int awayPoints, final double thresholdPercentage) {
    this(new Trendlines(awayPoints, thresholdPercentage));
  }

  public TrendlineBreakout(final Trendlines trendlines) {
    super(ONE,
          false,
          new Crossover(HIGH, trendlines, ZERO, ZERO),
          new Crossover(LOW, trendlines, ZERO, ONE));
  }

}
