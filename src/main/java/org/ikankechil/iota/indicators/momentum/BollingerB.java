/**
 * BollingerB.java  v0.1  19 December 2014 2:58:00 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.volatility.BollingerBands;

/**
 * Bollinger %b by John Bollinger
 *
 * <p>http://www.bollingerbands.com/services/bb/<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BollingerB extends BollingerBands {

  public BollingerB() {
    super();
  }

  public BollingerB(final int period, final double stdDev) {
    super(period, stdDev);
  }

  public BollingerB(final int period, final double stdDevUpper, final double stdDevLower) {
    super(period, stdDevUpper, stdDevLower);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series) {
    final List<TimeSeries> bollingerBands = super.generate(series);

    final TimeSeries upperBand = bollingerBands.get(ZERO);
    final double[] upper = upperBand.values();
    final double[] lower = bollingerBands.get(TWO).values();
    final double[] closes = series.values();

    // Formula
    // %b = (Close - Lower Band) / (Upper Band - Lower Band)
    final double[] b = new double[upperBand.size()];
    for (int i = ZERO, j = lookback; i < b.length; ++i, ++j) {
      b[i] = (closes[j] - lower[i]) / (upper[i] - lower[i]);
    }

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name, upperBand.dates(), b));
  }

}
