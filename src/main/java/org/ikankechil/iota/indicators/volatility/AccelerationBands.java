/**
 * AccelerationBands.java  v0.1 14 January 2015 9:46:27 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Price Headley's Acceleration Bands
 * <p>
 * http://www.bigtrends.com/accelerationbands.pdf
 * http://www2.wealth-lab.com/WL5Wiki/AccelerationBands.ashx
 * https://www.tradingtechnologies.com/help/x-study/technical-indicator-definitions/acceleration-bands-abands/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AccelerationBands extends AbstractIndicator {

  private final double        factor;

  private final int           MULTIPLIER  = 4000;
  private static final double FACTOR      = 0.001;

  private static final String UPPER_BAND  = "Upper Acceleration Band";
  private static final String MIDDLE_BAND = "Middle Acceleration Band";
  private static final String LOWER_BAND  = "Lower Acceleration Band";

  public AccelerationBands() {
    this(TWENTY, FACTOR);
  }

  public AccelerationBands(final int period, final double factor) {
    super(period, TA_LIB.smaLookback(period));
    throwExceptionIfNegative(factor);

    this.factor = MULTIPLIER * factor;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // Upperband = ( High * ( 1 + 2 * (((( High - Low )/(( High + Low ) / 2 )) * 1000 ) * Factor )))
    // Lowerband = ( Low  * ( 1 - 2 * (((( High - Low )/(( High + Low ) / 2 )) * 1000 ) * Factor )))
    // Factor = 0.001

    // Optimised version:
    // Upperband = (High * (1 + (High - Low)/(High + Low) * 4000 * Factor))
    // Lowerband = (Low  * (1 - (High - Low)/(High + Low) * 4000 * Factor))

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] upperBand = new double[size - lookback];
    final double[] middleBand = new double[upperBand.length];
    final double[] lowerBand = new double[upperBand.length];

    // compute SMA
    final RetCode outcome = TA_LIB.sma(ZERO,
                                       size - ONE,
                                       ohlcv.closes(),
                                       period,
                                       outBegIdx,
                                       outNBElement,
                                       middleBand);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO, j = lookback; i < upperBand.length; ++i, ++j) {
      final double high = ohlcv.high(j);
      final double low = ohlcv.low(j);
      final double ratio = ((high - low) / (high + low)) * factor;

      upperBand[i] = high * (ONE + ratio); // TODO SMA required here?
      lowerBand[i] = low  * (ONE - ratio);
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
