/**
 * MinimumMaximumPriceIndex.java  v0.2  27 January 2015 1:52:20 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
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
 * @version 0.2
 */
public class MinimumMaximumPriceIndex extends AbstractIndicator {

  private final MinimumPriceIndex min;
  private final MaximumPriceIndex max;

  private static final String     MININUM_PRICE_INDEX = "Minimum Price Index";
  private static final String     MAXIMUM_PRICE_INDEX = "Maximum Price Index";

  public MinimumMaximumPriceIndex(final int period) {
    super(period, (period - ONE));

    min = new MinimumPriceIndex(period);
    max = new MaximumPriceIndex(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final double[] minPriceIndices = min.generate(ohlcv).get(ZERO).values();
    final double[] maxPriceIndices = max.generate(ohlcv).get(ZERO).values();

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(MININUM_PRICE_INDEX, dates, minPriceIndices),
                         new TimeSeries(MAXIMUM_PRICE_INDEX, dates, maxPriceIndices));
  }

}
