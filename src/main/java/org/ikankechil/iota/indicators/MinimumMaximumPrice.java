/**
 * MinimumMaximumPrice.java  v0.2  27 January 2015 12:56:59 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
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
 * @version 0.2
 */
public class MinimumMaximumPrice extends AbstractIndicator {

  private final MinimumPrice  min;
  private final MaximumPrice  max;

  private static final String MININUM_PRICE = "Minimum Price";
  private static final String MAXIMUM_PRICE = "Maximum Price";

  public MinimumMaximumPrice(final int period) {
    super(period, (period - ONE));

    min = new MinimumPrice(period);
    max = new MaximumPrice(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final double[] minPrices = min.generate(ohlcv).get(ZERO).values();
    final double[] maxPrices = max.generate(ohlcv).get(ZERO).values();

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(MININUM_PRICE, dates, minPrices),
                         new TimeSeries(MAXIMUM_PRICE, dates, maxPrices));
  }

}
