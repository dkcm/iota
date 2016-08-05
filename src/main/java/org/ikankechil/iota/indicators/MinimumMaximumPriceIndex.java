/**
 * MinimumMaximumPriceIndex.java  v0.1  27 January 2015 1:52:20 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minimum and Maximum Price Index
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumMaximumPriceIndex extends AbstractIndicator {

  private static final String MININUM_PRICE_INDEX = "Minimum Price Index";
  private static final String MAXIMUM_PRICE_INDEX = "Maximum Price Index";

  public MinimumMaximumPriceIndex(final int period) {
    super(period, TA_LIB.minMaxIndexLookback(period));
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final int[] outMinIdx = new int[size - lookback];
    final int[] outMaxIdx = new int[outMinIdx.length];

    final RetCode outcome = TA_LIB.minMaxIndex(ZERO,
                                               size - ONE,
                                               ohlcv.closes(),
                                               period,
                                               outBegIdx,
                                               outNBElement,
                                               outMinIdx,
                                               outMaxIdx);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(MININUM_PRICE_INDEX, dates, toDoubles(outMinIdx)),
                         new TimeSeries(MAXIMUM_PRICE_INDEX, dates, toDoubles(outMaxIdx)));
  }

}
