/**
 * LaguerreRSICrossover.java  v0.1  18 September 2016 11:57:50 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.LaguerreRSI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 *
 *
 * <p>"A typical use of the Laguerre RSI is to buy after the line crosses back over
 * the 20 percent level and sell after the price crosses back down over the 80
 * percent level."
 *
 * <p>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LaguerreRSICrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = 0.2;
  private static final double OVERBOUGHT = 0.8;

  public LaguerreRSICrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public LaguerreRSICrossover(final double buy, final double sell) {
    super(new LaguerreRSI(), buy, sell);
  }

  public LaguerreRSICrossover(final double gamma) {
    this(gamma, OVERSOLD, OVERBOUGHT);
  }

  public LaguerreRSICrossover(final double gamma, final double buy, final double sell) {
    super(new LaguerreRSI(gamma), buy, sell);
  }

}
