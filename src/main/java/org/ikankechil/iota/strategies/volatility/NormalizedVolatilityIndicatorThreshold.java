/**
 * NormalizedVolatilityIndicatorThreshold.java  v0.1  14 April 2017 9:28:03 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.volatility;

import org.ikankechil.iota.indicators.volatility.NormalizedVolatilityIndicator;
import org.ikankechil.iota.strategies.Threshold;

/**
 *
 *
 * <p>Buy: NVI < 1.343<br>
 * Sell: NVI > 1.343<br>
 *
 * <p>http://edmond.mires.co/GES816/21-Normalized%20Volatility%20Indicator.pdf<br>
 * http://traders.com/Documentation/FEEDbk_docs/2010/08/TradersTips.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NormalizedVolatilityIndicatorThreshold extends Threshold {

  private static final double NVI = 1.343;

  public NormalizedVolatilityIndicatorThreshold() {
    this(NVI);
  }

  public NormalizedVolatilityIndicatorThreshold(final double threshold) {
    super(new NormalizedVolatilityIndicator(), threshold);
  }

}
