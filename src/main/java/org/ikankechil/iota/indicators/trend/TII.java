/**
 * TII.java  v0.1  21 January 2015 12:45:00 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Trend Intensity Index (TII) by M. H. Pee
 * <p>http://user42.tuxfamily.org/chart/manual/Trend-Intensity-Index.html<br>
 * https://www.forex-tsd.com/filedata/fetch?id=971632<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C06/113TRND.pdf<br>
 *
 * <p>"Pee recommended entering trades when levels of 80 on the upside or 20 on
 * the downside are reached. Lines are shown in Chart at those levels, as is 50
 * which is a neutral level."
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TII extends AbstractIndicator {

  private final int sma;

  public TII() {
    this(THIRTY, SIXTY);
  }

  public TII(final int period, final int sma) {
    super(period, sma + period - TWO);
    throwExceptionIfNegative(sma);
    if (sma <= period) {
      throw new IllegalArgumentException(String.format("sma <= period: %d > %d",
                                                       sma,
                                                       period));
    }

    this.sma = sma;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 30-day TII = (SD+)/((SD+)+(SD-))*100
    // where
    //   SD+ = Sum of up deviations of the last 30 days
    //   SD- = Sum of down deviations of the last 30 days

    final double[] averages = sma(values, sma);

    // compute up and down deviations
    final double[] ups = new double[averages.length];
    final double[] deviations = new double[ups.length];

    for (int i = ZERO, j = sma - ONE; i < averages.length; ++i, ++j) {
      final double deviation = values[j] - averages[i];
      if (deviation >= ZERO) {
        deviations[i] = ups[i] = deviation;
      }
      else {
        deviations[i] = -deviation;
      }
    }

    // compute sums
    final double[] sumUps = sum(period, ups);
    final double[] sumDeviations = sum(period, deviations);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double sumUp = sumUps[i];
      final double sumDeviation = sumDeviations[i];
      output[i] = HUNDRED_PERCENT * sumUp / sumDeviation;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
