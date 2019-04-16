/**
 * MinimumMaximumPrice.java  v0.4  27 January 2015 12:56:59 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Minimum and Maximum Price
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class MinimumMaximumPrice extends AbstractIndicator {

  private final Indicator min;
  private final Indicator max;

  public MinimumMaximumPrice(final int period) {
    super(period, (period - ONE));

    min = new MinimumPrice(period);
    max = new MaximumPrice(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    throwExceptionIfShort(series);

    final TimeSeries minPrices = min.generate(series, start).get(ZERO);
    final TimeSeries maxPrices = max.generate(series, start).get(ZERO);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(minPrices, maxPrices);
  }

}
