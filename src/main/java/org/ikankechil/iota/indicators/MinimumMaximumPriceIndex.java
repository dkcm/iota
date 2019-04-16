/**
 * MinimumMaximumPriceIndex.java  v0.3  27 January 2015 1:52:20 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Minimum and Maximum Price Index
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MinimumMaximumPriceIndex extends AbstractIndicator {

  private final Indicator min;
  private final Indicator max;

  public MinimumMaximumPriceIndex(final int period) {
    super(period, (period - ONE));

    min = new MinimumPriceIndex(period);
    max = new MaximumPriceIndex(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    final TimeSeries minPriceIndices = min.generate(ohlcv, start).get(ZERO);
    final TimeSeries maxPriceIndices = max.generate(ohlcv, start).get(ZERO);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(minPriceIndices, maxPriceIndices);
  }

}
