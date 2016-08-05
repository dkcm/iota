/**
 * TDI.java  v0.1  9 December 2015 12:21:31 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Trend Detection Index (TDI) by M. H. Pee
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C10/112TREN.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // 20-day TDI = (AV20) - ((SumAM40) - (SumAM20))
    // where
    //   AV20 = Absolute value of the sum of 20-day momenta of the last 20 days
    //   SumAM40 = Sum of 20-day absolute momenta of the last 40 days
    //   SumAM20 = Sum of 20-day absolute momenta of the last 20 days

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] closes = ohlcv.closes();

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
    final double[] absSumMomenta = new double[direction.length];
    for (int i = ZERO; i < absSumMomenta.length; ++i) {
      absSumMomenta[i] = Math.abs(direction[i]);
    }

    // compute sums of absolute momentum
    final double[] sumAbsMomenta1 = sum(period, absMomenta);
    final double[] sumAbsMomenta2 = sum(doublePeriod, absMomenta);

    // compute indicator
    final double[] tdi = new double[size - lookback];
    for (int i = ZERO, sam1 = sumAbsMomenta1.length - sumAbsMomenta2.length; // = period
         i < tdi.length;
         ++i, ++sam1) {
      tdi[i] = absSumMomenta[sam1] + sumAbsMomenta1[sam1] - sumAbsMomenta2[i];
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        tdi),
                         new TimeSeries(TDI_DIRECTION_INDICATOR,
                                        dates,
                                        Arrays.copyOfRange(direction,
                                                           period,
                                                           direction.length)));
  }

}
