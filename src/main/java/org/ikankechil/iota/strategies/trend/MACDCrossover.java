/**
 * MACDCrossover.java  v0.1  21 September 2016 12:57:11 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.trend;

import org.ikankechil.iota.indicators.trend.MACD;
import org.ikankechil.iota.strategies.CrossoverWithThreshold;

/**
 * MACD Crossover strategy.
 *
 * <p>Buy when MACD crosses over MACD signal line while under a threshold<br>
 * Sell when MACD crosses MACD signal line while above a threshold<br>
 *
 * <p>http://etfhq.com/blog/2013/02/26/macd-test-results/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MACDCrossover extends CrossoverWithThreshold {

  public MACDCrossover(final double threshold) {
    this(-threshold, threshold);
  }

  public MACDCrossover(final int fast, final int slow, final int signal, final double threshold) {
    this(fast, slow, signal, -threshold, threshold);
  }

  public MACDCrossover(final MACD macd, final double threshold) {
    this(macd, -threshold, threshold);
  }

  public MACDCrossover(final double buy, final double sell) {
    this(TWELVE, TWENTY_SIX, NINE, buy, sell);
  }

  public MACDCrossover(final int fast, final int slow, final int signal, final double buy, final double sell) {
    this(new MACD(fast, slow, signal), buy, sell);
  }

  public MACDCrossover(final MACD macd, final double buy, final double sell) {
    super(macd, buy, sell);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
