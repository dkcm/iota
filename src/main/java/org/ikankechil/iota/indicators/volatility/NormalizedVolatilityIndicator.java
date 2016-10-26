/**
 * NormalizedVolatilityIndicator.java  v0.3  12 August 2015 1:10:10 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

/**
 * Normalized Volatility Indicator by Rajesh Kayakkal
 *
 * <p>http://edmond.mires.co/GES816/21-Normalized%20Volatility%20Indicator.pdf<br>
 * http://traders.com/Documentation/FEEDbk_docs/2010/08/TradersTips.html<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class NormalizedVolatilityIndicator extends NATR {

  private static final int AVE_DAYS_IN_QTR = SIXTY_FOUR;

  public NormalizedVolatilityIndicator() {
    super(AVE_DAYS_IN_QTR); // average number of trading days in a quarter

    // Formula:
    // NormalizedVolatilityIndicator = 64-Day average true range / End-of-day price * 100
  }

}
