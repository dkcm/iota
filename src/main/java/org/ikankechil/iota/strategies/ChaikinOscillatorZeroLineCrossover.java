/**
 * ChaikinOscillatorZeroLineCrossover.java  v0.2  16 September 2016 12:46:43 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.indicators.volume.ChaikinOscillator;

/**
 * Signals when the Chaikin Oscillator crosses over / under zero.
 * <p>
 * Buys when the Chaikin Oscillator crosses over zero<br>
 * Sells when the Chaikin Oscillator crosses under zero<br>
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ChaikinOscillatorZeroLineCrossover extends ZeroLineCrossover {

  public ChaikinOscillatorZeroLineCrossover() {
    this(SIX, TWENTY);
  }

  public ChaikinOscillatorZeroLineCrossover(final int fast, final int slow) {
    this(new ChaikinOscillator(fast, slow));
  }

  public ChaikinOscillatorZeroLineCrossover(final ChaikinOscillator chaikinOscillator) {
    super(chaikinOscillator);
  }

}
