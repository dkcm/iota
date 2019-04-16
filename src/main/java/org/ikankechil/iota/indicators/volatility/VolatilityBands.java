/**
 * VolatilityBands.java  v0.2  26 September 2016 10:56:51 am
 *
 * Copyright © 2016-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.MA;
import org.ikankechil.iota.indicators.trend.ZeroLagTEMA;

/**
 * Volatility Bands by David Rooke
 *
 * <p>References:
 * <li>http://www.futuresmag.com/2010/04/30/fixing-bollinger-bands
 * <li>http://www.futuresmag.com/2010/04/30/fixing-bollinger-bands-codes-and-results
 * <li>https://lcchong.files.wordpress.com/2011/06/fixing-the-bollinger-bands.pdf
 * <li>https://c.mql5.com/forextsd/forum/72/fixing_the_bollinger_bands.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VolatilityBands extends AbstractIndicator {

  private final MA            zltema;
  private final Indicator     stdDev;

  private static final String UPPER_BAND  = "Volatility Bands Upper Band";
  private static final String MIDDLE_BAND = "Volatility Bands Middle Band";
  private static final String LOWER_BAND  = "Volatility Bands Lower Band";

  public VolatilityBands() {
    this(TWENTY);
  }

  public VolatilityBands(final int period) {
    this(period, ONE);
  }

  public VolatilityBands(final int period, final double stdDev) {
    this(new ZeroLagTEMA(period), new StandardDeviation(period, stdDev));
  }

  public VolatilityBands(final MA ma, final StandardDeviation stdDev) {
    super(ma.lookback() + stdDev.lookback());

    zltema = ma;
    this.stdDev = stdDev;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    //
    // The requirements for calculating the volatility bands are simple:
    // 1. Any low-lag moving average
    // 2. A standard deviation calculation based on that moving average
    //
    // The moving average used here is a lag-adjusted triple exponential moving average:
    // Function ZL_TEMA(TEMA1 As BarArray, Length, OffSet) As BarArray
    //   Dim TEMA2 As BarArray
    //   Dim Diff
    //   TEMA2 = TEMA(TEMA1, Length, OffSet)
    //   Diff = TEMA1 - TEMA2
    //   ZL_TEMA = MA_TEMA1 + Diff
    // End Function
    //
    // Standard deviation is calculated using the following function:
    // Function StdDevPlus(Series As BarArray, SeriesMA As BarArray, Length As Integer) As Double
    //   Dim i
    //   Dim SumSquares
    //   For i = 0 To Length -1
    //   SumSquares = SumSquares + (Deviation(Series[i], SeriesMA[i])^2)
    //   Next
    //   StdDevPlus = Sqr(SumSquares /(Length -1))
    // End Function

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute low-lag moving average
    final TimeSeries zltemas = zltema.generate(ohlcv, start).get(ZERO);

    // compute standard deviation of moving average
    final double[] upperBand = stdDev.generate(zltemas).get(ZERO).values();

    final double[] middleBand = Arrays.copyOfRange(zltemas.values(), stdDev.lookback(), zltemas.size());
    final double[] lowerBand = new double[upperBand.length];

    for (int i = ZERO; i < middleBand.length; ++i) {
      final double middle = middleBand[i];
      lowerBand[i] = middle - upperBand[i];
      upperBand[i] += middle;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, middleBand),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
