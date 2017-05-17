/**
 * GoldenCross.java  v0.1  25 April 2017 10:12:06 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.EMA;
import org.ikankechil.iota.strategies.Crossover;

/**
 * Golden Cross strategy.
 *
 * <p>Buy when fast MA crosses over slow MA<br>
 * Sell when slow MA crosses under fast MA<br>
 *
 * <p>http://etfhq.com/blog/2013/01/15/golden-cross-which-is-the-best/<br>
 * http://www.investopedia.com/terms/g/goldencross.asp
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GoldenCross extends Crossover {

  public GoldenCross() {
    this(THIRTEEN, FORTY_EIGHT);
    // other good combinations:
    // (12, 50)
  }

  public GoldenCross(final int fast, final int slow) {
    this(new EMA(fast), new EMA(slow));
  }

  public GoldenCross(final Indicator fast, final Indicator slow) {
    super(fast, slow);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
