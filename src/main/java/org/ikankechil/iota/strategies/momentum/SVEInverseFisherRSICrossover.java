/**
 * SVEInverseFisherRSICrossover.java  v0.1  10 August 2017 6:01:41 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.SVEInverseFisherRSI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * SVE Inverse Fisher RSI crossover by Sylvain Vervoort
 *
 * <p>References:
 * <li>http://xa.yimg.com/kq/groups/16789226/301268564/name/192verv-1.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SVEInverseFisherRSICrossover extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = 12.0;
  private static final double OVERBOUGHT = 88.0;

  public SVEInverseFisherRSICrossover() {
    this(OVERSOLD, OVERBOUGHT);
  }

  public SVEInverseFisherRSICrossover(final double buy, final double sell) {
    this(new SVEInverseFisherRSI(), buy, sell);
  }

  public SVEInverseFisherRSICrossover(final int rsi, final int ema) {
    this(rsi, ema, OVERSOLD, OVERBOUGHT);
  }

  public SVEInverseFisherRSICrossover(final int rsi, final int ema, final double buy, final double sell) {
    this(new SVEInverseFisherRSI(rsi, ema), buy, sell);
  }

  public SVEInverseFisherRSICrossover(final SVEInverseFisherRSI sveInverseFisherRSI) {
    super(sveInverseFisherRSI, OVERSOLD, OVERBOUGHT);
  }

  public SVEInverseFisherRSICrossover(final SVEInverseFisherRSI sveInverseFisherRSI, final double buy, final double sell) {
    super(sveInverseFisherRSI, buy, sell);
  }

}
