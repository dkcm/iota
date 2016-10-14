/**
 * TCF.java  v0.1  9 December 2015 4:13:48 PM
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
 * Trend Continuation Factor (TCF) by M.H. Pee
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C03/050TREN.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TCF extends AbstractIndicator {

  private static final String POSITIVE_TCF = "TCF+";
  private static final String NEGATIVE_TCF = "TCF-";

  public TCF() {
    this(THIRTY_FIVE);
  }

  public TCF(final int period) {
    super(period, period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // 35-day +TCF = Sum of (+change) for the last 35 days - sum of (-CF) for the last 35 days
    // 35-day –TCF = Sum of (-change) for the last 35 days - sum of (+CF) for the last 35 days
    //
    // Algorithm:
    // 1. compute positive and negative changes in closes
    // 2. sum of positive and negative changes
    // 3. compute positive and negative continuation factors (by reusing changes)
    // 4. sum of positive and negative continuation factors
    // 5. compute positive and negative trend continuation factors

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute positive and negative changes in closes
    final double[] positiveChanges = new double[size - ONE];
    final double[] negativeChanges = new double[positiveChanges.length];
    computeChanges(ohlcv.closes(), positiveChanges, negativeChanges);

    // sum of positive and negative changes
    final double[] pTCF = sum(period, positiveChanges);
    final double[] nTCF = sum(period, negativeChanges);

    // compute positive and negative continuation factors (by reusing changes)
    computeContinuationFactors(positiveChanges, negativeChanges);

    // sum of positive and negative continuation factors
    final double[] sumNCF = sum(period, negativeChanges);
    final double[] sumPCF = sum(period, positiveChanges);

    // compute positive and negative trend continuation factors
    for (int i = ZERO; i < pTCF.length; ++i) {
      pTCF[i] -= sumNCF[i];
      nTCF[i] -= sumPCF[i];
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(POSITIVE_TCF, dates, pTCF),
                         new TimeSeries(NEGATIVE_TCF, dates, nTCF));
  }

  private static final void computeChanges(final double[] closes,
                                           final double[] positiveChanges,
                                           final double[] negativeChanges) {
    int c = ZERO;
    double previous = closes[c];
    for (int i = ZERO; ++c < closes.length; ++i) {
      final double current = closes[c];
      final double change = current - previous;
      if (change >= ZERO) {
        positiveChanges[i] = change;
      }
      else {
        negativeChanges[i] = -change;
      }
      previous = current;
    }
  }

  private static final void computeContinuationFactors(final double[] positiveChanges,
                                                       final double[] negativeChanges) {
    for (int today = ONE, yesterday = ZERO;
         today < negativeChanges.length;
         ++today, ++yesterday) {
      if (positiveChanges[today] != ZERO) {
        positiveChanges[today] += positiveChanges[yesterday];
      }
      if (negativeChanges[today] != ZERO) {
        negativeChanges[today] += negativeChanges[yesterday];
      }
    }
  }

}
