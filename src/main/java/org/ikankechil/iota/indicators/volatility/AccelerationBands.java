/**
 * AccelerationBands.java  v0.3  14 January 2015 9:46:27 AM
 *
 * Copyright © 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.SMA;

/**
 * Acceleration Bands by Price Headley
 *
 * <p>References:
 * <li>http://www.bigtrends.com/education/inside-bigtrends-acceleration-bands-indicator/<br>
 * <li>http://www2.wealth-lab.com/WL5Wiki/AccelerationBands.ashx<br>
 * <li>https://www.tradingtechnologies.com/help/x-study/technical-indicator-definitions/acceleration-bands-abands/<br>
 * <li>http://members.bigtrends.com/wp-content/uploads/2014/05/Acceleration-Bands-Rules.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class AccelerationBands extends AbstractIndicator {

  private final double        factor;
  private final Indicator     sma;

  private static final int    MULTIPLIER  = 4000;
  private static final double FACTOR      = 0.001;

  private static final String UPPER_BAND  = "Upper Acceleration Band";
  private static final String MIDDLE_BAND = "Middle Acceleration Band";
  private static final String LOWER_BAND  = "Lower Acceleration Band";

  public AccelerationBands() {
    this(TWENTY, FACTOR);
  }

  public AccelerationBands(final int period, final double factor) {
    super(period, (period - ONE));
    throwExceptionIfNegative(factor);

    this.factor = MULTIPLIER * factor;
    sma = new SMA(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // Upper band = SMA( High * ( 1 + 2 * (((( High - Low )/(( High + Low ) / 2 )) * 1000 ) * Factor )))
    // Lower band = SMA( Low  * ( 1 - 2 * (((( High - Low )/(( High + Low ) / 2 )) * 1000 ) * Factor )))
    // Factor = 0.001

    // Optimised version:
    // Upper band = SMA(High * (1 + (High - Low)/(High + Low) * 4000 * Factor))
    // Lower band = SMA(Low  * (1 - (High - Low)/(High + Low) * 4000 * Factor))

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute upper and lower bands
    final double[] upper = new double[size];
    final double[] lower = new double[upper.length];
    for (int i = ZERO; i < size; ++i) {
      final double high = ohlcv.high(i);
      final double low = ohlcv.low(i);
      final double ratio = ((high - low) / (high + low)) * factor;

      upper[i] = high * (ONE + ratio);
      lower[i] = low  * (ONE - ratio);
    }

    // compute indicator
    final String[] unused = new String[size];
    final double[] upperBand = sma.generate(new TimeSeries(EMPTY, unused, upper)).get(ZERO).values();
    final double[] lowerBand = sma.generate(new TimeSeries(EMPTY, unused, lower)).get(ZERO).values();
    final double[] middleBand = new double[size - lookback];
    for (int i = ZERO; i < middleBand.length; ++i) {
      middleBand[i] = (upperBand[i] + lowerBand[i]) * HALF;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
