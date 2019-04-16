/**
 * KDJ.java  v0.2  26 January 2015 11:20:23 pm
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * KDJ indicator
 *
 * <p>References:
 * <li>http://www.wisestocktrader.com/indicators/320-stochastic-j-kdj
 * <li>https://news.cqg.com/blogs/indicators/2016/08/kdj-indicator<br>
 * <br>
 *
 * <p>Buy: when %J < 0, with %K and %D at the bottom range<br>
 * Sell: when %J > 100, with %K and %D at the top range<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KDJ extends Stochastic {

  private static final String J = "Stochastic %J";

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // RSV = (CLOSE - LLV(LOW, N))/(HHV(HIGH, N)- LLV(LOW, N)) * 100
    // K   = EMA(RSV, M1)
    // D   = EMA(K, M2)
    // J   = 3 * K - 2 * D

    final List<TimeSeries> stochastic = super.generate(ohlcv, start);

    final TimeSeries k = stochastic.get(ZERO);
    final TimeSeries d = stochastic.get(ONE);

    // compute indicator
    final TimeSeries j = computeJ(k, d);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(k, d, j);
  }

  private static TimeSeries computeJ(final TimeSeries k, final TimeSeries d) {
    final double[] j = new double[k.size()];
    for (int i = ZERO; i < j.length; ++i) {
      j[i] = (THREE * k.value(i)) - (TWO * d.value(i));
    }
    return new TimeSeries(J, k.dates(), j);
  }

}
