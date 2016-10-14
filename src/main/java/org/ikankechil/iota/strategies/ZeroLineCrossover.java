/**
 * ZeroLineCrossover.java  v0.1  20 September 2016 5:40:38 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.indicators.Indicator;

/**
 * Signals when an indicator crosses over / under zero.
 *
 * <p>Buys when indicator crosses over zero<br>
 * Sells when indicator crosses under zero<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLineCrossover extends ThresholdCrossover {

  private static final int ZERO_LINE = ZERO;

  public ZeroLineCrossover(final Indicator indicator) {
    super(indicator, ZERO_LINE);
  }

  public ZeroLineCrossover(final Indicator indicator, final int indicatorIndex) {
    super(indicator, indicatorIndex, ZERO_LINE);
  }

}
