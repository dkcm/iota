/**
 * VortexCrossover.java  v0.1  7 October 2016 11:58:07 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.trend.Vortex;
import org.ikankechil.iota.strategies.Crossover;

/**
 * Vortex Indicator Crossover strategy.
 *
 * <p>http://www.traders.com/Reprints/PDF_reprints/VFX_VORTEX.PDF<br>
 *
 * <p>Buy when +VI crosses over -VI
 * Sell when +VI crosses under -VI
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VortexCrossover extends Crossover {

  public VortexCrossover(final int period) {
    this(new Vortex(period));
  }

  public VortexCrossover(final Vortex vortex) {
    super(vortex);
  }

}
