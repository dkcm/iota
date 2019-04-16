/**
 * TDI.java  v0.2  9 December 2015 12:21:31 am
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Trend Detection Index (TDI) by M. H. Pee
 *
 * <p>References:
 * <li>https://c.mql5.com/forextsd/forum/63/trend_detection_index.pdf
 * <li>http://traders.com/Documentation/FEEDbk_docs/2001/10/Abstracts_new/Pee/pee.html
 * <li>https://www.linnsoft.com/techind/trend-detection-index-tdi
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C10/112TREN.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TDI extends AbstractIndicator {
  // Strategy:
  // if TDI > 0
  //   if direction > 0, then buy
  //   else sell
  // else do nothing

  private final int           doublePeriod;

  private static final String TDI_DIRECTION_INDICATOR = "TDI Direction Indicator";

  public TDI() {
    this(TWENTY);
  }

  public TDI(final int period) {
    super(period, period * THREE - ONE);

    doublePeriod = period << ONE; // = 2 * period
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    // Formula:
    // 20-day TDI = (AV20) - ((SumAM40) - (SumAM20))
    // where
    //   AV20 = Absolute value of the sum of 20-day momenta of the last 20 days
    //   SumAM40 = Sum of 20-day absolute momenta of the last 40 days
    //   SumAM20 = Sum of 20-day absolute momenta of the last 20 days

    throwExceptionIfShort(series);
    final int size = series.size();

    final double[] closes = series.values();

    // compute momentum and absolute momentum
    final double[] momenta = new double[size - period];
    final double[] absMomenta = new double[momenta.length];
    for (int i = ZERO, v = period; v < size; ++i, ++v) {
      final double momentum = closes[v] - closes[i];
      momenta[i] = momentum;
      absMomenta[i] = Math.abs(momentum);
    }

    // compute direction indicator (sum of momentum) and absolute sum of momentum
    final double[] direction = sum(period, momenta);
    final double[] absSumMomenta = absSumMomenta(direction);

    // compute sums of absolute momentum
    final double[] sumAbsMomenta1 = sum(period, absMomenta);
    final double[] sumAbsMomenta2 = sum(doublePeriod, absMomenta);

    // compute indicator
    final double[] tdi = tdi(absSumMomenta, sumAbsMomenta1, sumAbsMomenta2);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        tdi),
                         new TimeSeries(TDI_DIRECTION_INDICATOR,
                                        dates,
                                        Arrays.copyOfRange(direction,
                                                           period,
                                                           direction.length)));
  }

  private static double[] absSumMomenta(final double[] direction) {
    final double[] absSumMomenta = new double[direction.length];
    for (int i = ZERO; i < absSumMomenta.length; ++i) {
      absSumMomenta[i] = Math.abs(direction[i]);
    }
    return absSumMomenta;
  }

  private static double[] tdi(final double[] absSumMomenta,
                              final double[] sumAbsMomenta1,
                              final double[] sumAbsMomenta2) {
    final double[] tdi = new double[sumAbsMomenta2.length];
    for (int i = ZERO, sam1 = sumAbsMomenta1.length - sumAbsMomenta2.length; // = period
         i < tdi.length;
         ++i, ++sam1) {
      tdi[i] = absSumMomenta[sam1] + sumAbsMomenta1[sam1] - sumAbsMomenta2[i];
    }
    return tdi;
  }

}
