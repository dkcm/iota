/**
 * MinimumMaximumPrice.java v0.1 27 January 2015 12:56:59 PM
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
 * Minimum and Maximum Price
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumMaximumPrice extends AbstractIndicator {

  private static final String MININUM_PRICE = "Minimum Price";
  private static final String MAXIMUM_PRICE = "Maximum Price";

  public MinimumMaximumPrice(final int period) {
    super(period, TA_LIB.minMaxLookback(period));
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] outMin = new double[size - lookback];
    final double[] outMax = new double[outMin.length];

    final RetCode outcome = TA_LIB.minMax(ZERO,
                                          size - ONE,
                                          ohlcv.closes(),
                                          period,
                                          outBegIdx,
                                          outNBElement,
                                          outMin,
                                          outMax);
    throwExceptionIfBad(outcome, ohlcv);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(MININUM_PRICE, dates, outMin),
                         new TimeSeries(MAXIMUM_PRICE, dates, outMax));
  }

}
