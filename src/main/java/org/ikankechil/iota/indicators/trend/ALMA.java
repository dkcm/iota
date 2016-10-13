/**
 * ALMA.java  v0.1  11 October 2016 12:00:21 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.EhlersFilter;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Arnaud Legoux Moving Average (ALMA) by Arnaud Legoux, Dimitrios Kouzis-Loukas
 * and Anthony Cascino
 * <p>
 * http://www.arnaudlegoux.com/img/ALMA-Arnaud-Legoux-Moving-Average.pdf<br>
 * http://www.wisestocktrader.com/indicators/1477-arnaud-legoux-moving-average<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ALMA extends EhlersFilter {

  // cache to reduce coefficient recalculation
  private final double[]      weights;
  private final double        norm;

  private static final double OFFSET = 0.85;

  public ALMA() {
    this(NINE);
  }

  public ALMA(final int period) {
    this(period, SIX, OFFSET);
  }

  /**
   *
   *
   * @param period
   * @param sigma changes filter shape -- increase / decrease to make it wider /
   *          more focused
   * @param offset trades-off smoothness with responsiveness (0.0, 1.0)
   */
  public ALMA(final int period, final double sigma, final double offset) {
    super(period, ZERO, period - ONE);
    throwExceptionIfNegative(sigma, offset);

    // compute weights and norm
    // weight = e^-[(i - m)^2 / 2s^2]
    // norm = sum of weights
    final int m = (int) (offset * (period - ONE));
    final double s = period / sigma;
    final double inverseVariance = HALF / (s * s);

    double sum = ZERO;
    weights = new double[period];
    for (int i = ZERO, i_m = i - m; i < period; ++i, ++i_m) {
      sum += weights[i] = Math.exp(-i_m * i_m * inverseVariance);
    }
    norm = sum;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return compute(start,
                   end,
                   ohlcv.closes(),
                   outBegIdx,
                   outNBElement,
                   output);
  }

  @Override
  protected double[] coefficients(final int index, final double... values) {
    return weights;
  }

  @Override
  protected double sumOf(final double... coefficients) {
    return norm;
  }

}
