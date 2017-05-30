/**
 * PGOCrossover.java  v0.1  30 May 2017 5:49:40 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.PGO;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * PGO crossover by Mark Johnson
 *
 * <p>"Mark Johnson's approach was to use it as a break-out system for longer
 * term trades. If the PGO rises above 3.0 then go long, or below -3.0 then go
 * short, and in both cases exit on returning to zero (which is a close back at
 * the SMA)."
 *
 * <p>http://user42.tuxfamily.org/chart/manual/Pretty-Good-Oscillator.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PGOCrossover extends ThresholdCrossover {

  // thresholds
  private static final double BUY  = 3.0;
  private static final double SELL = -3.0;

  public PGOCrossover() {
    this(BUY, SELL);
  }

  public PGOCrossover(final double buy, final double sell) {
    this(new PGO(), buy, sell);
  }

  public PGOCrossover(final int period) {
    this(period, BUY, SELL);
  }

  public PGOCrossover(final int period, final double buy, final double sell) {
    this(new PGO(period), buy, sell);
  }

  public PGOCrossover(final PGO pgo) {
    this(pgo, BUY, SELL);
  }

  public PGOCrossover(final PGO pgo, final double buy, final double sell) {
    super(pgo, buy, sell);
  }

}
