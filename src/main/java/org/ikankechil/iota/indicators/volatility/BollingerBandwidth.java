/**
 * BollingerBandwidth.java  v0.1  19 December 2014 1:59:47 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Bollinger Bandwidth
 * <p>
 * http://www.bollingerbands.com/services/bb/
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_band_width
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BollingerBandwidth extends BollingerBands {

  public BollingerBandwidth() {
    super();
  }

  public BollingerBandwidth(final int period, final double stdDev) {
    super(period, stdDev);
  }

  public BollingerBandwidth(final int period, final double stdDevUpper, final double stdDevLower) {
    super(period, stdDevUpper, stdDevLower);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    final List<TimeSeries> bollingerBands = super.generate(ohlcv);

    final TimeSeries upper = bollingerBands.get(ZERO);
    final TimeSeries middle = bollingerBands.get(ONE);
    final TimeSeries lower = bollingerBands.get(TWO);

    // Formula
    // Bandwidth = (Upper Band - Lower Band) / Middle Band
    final double[] bandwidth = new double[upper.size()];
    for (int i = ZERO; i < bandwidth.length; ++i) {
      bandwidth[i] = (upper.value(i) - lower.value(i)) / middle.value(i);
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, upper.dates(), bandwidth));
  }

}
