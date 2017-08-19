/**
 * SimpleBollingerBandStrategy.java  v0.1  18 August 2017 11:10:48 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.volatility;

import org.ikankechil.iota.indicators.ClosePrice;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.BollingerBands;
import org.ikankechil.iota.strategies.Crossover;

/**
 * Simple Bollinger Band strategy
 *
 * <p>Buy when price crosses over the lower Bollinger band from below<br>
 * Sell when price crosses under the upper Bollinger band from above<br>
 *
 * <p>BB band strategy + Stop loss
 * BUY on T+1: close < lower BB band && no BB band squeeze (Note: close < lower BB band == %B < 0)
 * SELL: high > upper BB band
 *
 * <p>BB band squeeze: "A squeeze candidate is identified when the bandwidth is at
 * a six-month low-low value."
 *
 * <p>References:
 * <li>http://www.investopedia.com/articles/trading/07/bollinger.asp<br>
 * <li>http://www.investopedia.com/articles/technical/04/030304.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SimpleBollingerBandStrategy extends Crossover {

  private static final Indicator FAST       = new ClosePrice();

  private static final int       UPPER_BAND = ZERO;
  private static final int       LOWER_BAND = TWO;

  public SimpleBollingerBandStrategy() {
    this(new BollingerBands());
  }

  public SimpleBollingerBandStrategy(final int period, final double stdDev) {
    this(new BollingerBands(period, stdDev));
  }

  public SimpleBollingerBandStrategy(final int period, final double stdDevUpper, final double stdDevLower) {
    this(new BollingerBands(period, stdDevUpper, stdDevLower));
  }

  public SimpleBollingerBandStrategy(final BollingerBands bb) {
    super(FAST, bb, ZERO, LOWER_BAND, ZERO, UPPER_BAND);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
