/**
 * QStickZeroLineCrossover.java  v0.1  19 December 2016 11:13:27 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.QStick;
import org.ikankechil.iota.strategies.ZeroLineCrossover;

/**
 * Signals when the <code>QStick</code> crosses over / under zero.
 *
 * <p>Buys when the QStick crosses over zero<br>
 * Sells when the QStick crosses under zero<br>
 *
 * <p>http://tradingsim.com/blog/qstick/<br>
 * http://www.precisiontradingsystems.com/QSTICK.htm<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class QStickZeroLineCrossover extends ZeroLineCrossover {

  public QStickZeroLineCrossover(final QStick qStick) {
    super(qStick);
  }

}
