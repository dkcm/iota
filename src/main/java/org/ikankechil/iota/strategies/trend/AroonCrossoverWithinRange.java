/**
 * AroonCrossoverWithinRange.java  v0.1  21 September 2014 11:43:59 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.indicators.trend.Aroon;

/**
 * Aroon crossover occurring within a range.
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon
 * http://www.investopedia.com/articles/trading/06/aroon.asp
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AroonCrossoverWithinRange extends Crossover {

  private final double        weak;
  private final double        strong;

  // thresholds
  private static final double WEAK        = 30.0;
  private static final double STRONG      = 70.0;

  private static final int    TWENTY_FIVE = 25;

  public AroonCrossoverWithinRange() {
    this(TWENTY_FIVE);
  }

  public AroonCrossoverWithinRange(final int period) {
    this(period, WEAK, STRONG);
  }

  public AroonCrossoverWithinRange(final int period, final double weak, final double strong) {
    super(new Aroon(period));
    if (weak < 0 || strong < 0 || weak > 100 || strong > 100) {
      throw new IllegalArgumentException();
    }

    this.weak = weak;
    this.strong = strong;
  }

  @Override
  public boolean buy(final double... doubles) {
    // aroon up within range
    return withinRange(doubles[TWO]) && super.buy(doubles);
  }

  @Override
  public boolean sell(final double... doubles) {
    // aroon up within range
    return withinRange(doubles[TWO]) && super.sell(doubles);
  }

  private final boolean withinRange(final double value) {
    return (value > weak) && (value < strong);
  }

}
