/**
 * CGOscillatorCrossover.java  v0.1  1 April 2018 12:38:56 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.CGOscillator;
import org.ikankechil.iota.strategies.Crossover;

/**
 * <code>CGOscillator</code> crossover strategy.
 *
 * <p>"Since the CG is smoothed, an effective crossover signal is produced simply by delaying the CG by one bar."
 *
 * <p>References:
 * <li>http://www.mesasoftware.com/papers/TheCGOscillator.pdf<br>
 * <li>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C05/088CENT.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CGOscillatorCrossover extends Crossover {

  public CGOscillatorCrossover() {
    this(new CGOscillator());
  }

  public CGOscillatorCrossover(final int period) {
    this(new CGOscillator(period));
  }

  public CGOscillatorCrossover(final CGOscillator cgo) {
    super(cgo);
  }

}
