/**
 * ChaikinOscillatorThresholdCrossover.java  v0.1  20 January 2017 9:44:39 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.volume;

import org.ikankechil.iota.indicators.volume.ChaikinOscillator;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 * Signals when the Chaikin Oscillator crosses over / under a threshold.
 *
 * <p>Buys when the Chaikin Oscillator crosses over a threshold<br>
 * Sells when the Chaikin Oscillator crosses under a threshold<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChaikinOscillatorThresholdCrossover extends ThresholdCrossover {

  public ChaikinOscillatorThresholdCrossover(final double threshold) {
    this(threshold, -threshold);
  }

  public ChaikinOscillatorThresholdCrossover(final double buy, final double sell) {
    this(new ChaikinOscillator(), buy, sell);
  }

  public ChaikinOscillatorThresholdCrossover(final int fast, final int slow, final double buy, final double sell) {
    this(new ChaikinOscillator(fast, slow), buy, sell);
  }

  public ChaikinOscillatorThresholdCrossover(final ChaikinOscillator chaikinOscillator, final double buy, final double sell) {
    super(chaikinOscillator, buy, sell);
  }

}
