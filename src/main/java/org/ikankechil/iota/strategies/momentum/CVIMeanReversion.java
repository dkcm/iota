/**
 * CVIMeanReversion.java  v0.2  16 September 2016 1:07:14 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import org.ikankechil.iota.indicators.momentum.CVI;
import org.ikankechil.iota.strategies.ThresholdCrossover;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CVIMeanReversion extends ThresholdCrossover {

  // thresholds
  private static final double OVERSOLD   = -8.0;
  private static final double OVERBOUGHT = 8.0;

  public CVIMeanReversion(final int period) {
    this(period, OVERSOLD, OVERBOUGHT);
  }

  public CVIMeanReversion(final int period, final double oversold, final double overbought) {
    super(new CVI(period), oversold, overbought);
  }

}
