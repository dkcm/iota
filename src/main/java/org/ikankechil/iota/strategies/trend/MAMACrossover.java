/**
 * MAMACrossover.java  v0.2  11 January 2017 4:41:26 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.trend.MAMA;
import org.ikankechil.iota.strategies.Crossover;

/**
 * MAMA Crossover strategy.
 *
 * <p>http://www.mesasoftware.com/papers/MAMA.pdf<br>
 *
 * <p>Buy when MAMA crosses over FAMA<br>
 * Sell when MAMA crosses under FAMA<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MAMACrossover extends Crossover {

  public MAMACrossover() {
    this(new MAMA());
  }

  public MAMACrossover(final double fast, final double slow) {
    this(new MAMA(fast, slow));
  }

  public MAMACrossover(final MAMA mama) {
    super(mama);
  }

}
