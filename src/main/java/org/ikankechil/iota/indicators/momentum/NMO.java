/**
 * NMO.java  v0.2  29 June 2017 11:28:45 pm
 *
 * Copyright © 2017-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.momentum.CMO.*;

/**
 * Net Momentum Oscillator (NMO) by Tushar Chande and Stanley Kroll
 *
 * <p>References:
 * <li>http://www.sierrachart.com/Download.php?Folder=SupportBoard&download=1446<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class NMO extends RSI {

  public NMO() {
    this(TWENTY);
  }

  public NMO(final int period) {
    super(period);
  }

  @Override
  protected double computeRSI(final double sumGain, final double sumLoss) {
    // Formulae:
    // 1. NMO = 100 * (Su - Sd) / (Su + Sd)
    // 2. NMO = 100 * Momentum / |Momentum|
    // 3. NMO = 2 * RSI - 100
    return cmo(sumGain, sumLoss);
  }

}
