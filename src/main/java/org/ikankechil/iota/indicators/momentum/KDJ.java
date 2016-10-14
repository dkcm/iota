/**
 * KDJ.java  v0.1  26 January 2015 11:20:23 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * KDJ indicator
 *
 * <p>http://www.wisestocktrader.com/indicators/320-stochastic-j-kdj
 *
 * @author Daniel
 * @version 0.1
 */
public class KDJ extends Stochastic {

  private static final String J = "Stochastic %J";

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // RSV = (CLOSE - LLV(LOW, N))/(HHV(HIGH, N)- LLV(LOW, N)) * 100
    // K   = EMA(RSV, M1)
    // D   = EMA(K, M2)
    // J   = 3 * K - 2 * D

    final List<TimeSeries> stochastic = super.generate(ohlcv);

    final TimeSeries k = stochastic.get(ZERO);
    final TimeSeries d = stochastic.get(ONE);

    // compute indicator
    final double[] j = new double[k.size()];
    for (int i = ZERO; i < j.length; ++i) {
      j[i] = (THREE * k.value(i)) - (TWO * d.value(i));
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(k, d, new TimeSeries(J, k.dates(), j));
  }

}
