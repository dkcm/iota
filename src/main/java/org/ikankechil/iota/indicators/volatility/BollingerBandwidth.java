/**
 * BollingerBandwidth.java  v0.2  19 December 2014 1:59:47 PM
 *
 * Copyright © 2014-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Bollinger Bandwidth
 *
 * <p>References:
 * <li>http://www.bollingerbands.com/services/bb/
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_band_width<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    final List<TimeSeries> bollingerBands = super.generate(ohlcv, start);

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
