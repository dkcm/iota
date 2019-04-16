/**
 * EhlersFilter.java  v0.4  7 July 2015 5:57:45 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static org.ikankechil.iota.indicators.MedianPrice.*;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ehlers Filter by John Ehlers.  Nonlinear FIR, Order Statistic (OS) filters.
 *
 * <p>References:
 * <li>http://www.mesasoftware.com/papers/EhlersFilters.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C04/040NON.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public abstract class EhlersFilter extends AbstractIndicator {

  private final int           priceOffset;

  private static final String USING_MEDIAN_PRICE = "Using median price";

  public EhlersFilter(final int period, final int priceOffset, final int lookback) {
    super(period, lookback);
    throwExceptionIfNegative(priceOffset);

    this.priceOffset = priceOffset;
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
                   values(ohlcv),
                   outBegIdx,
                   outNBElement,
                   output);
  }

  private static double[] values(final OHLCVTimeSeries ohlcv) {
    final double[] values = new double[ohlcv.size()];
    // mid-point of high and low = median
    for (int i = ZERO; i < values.length; ++i) {
      values[i] = medianPrice(ohlcv.high(i), ohlcv.low(i));
    }
    logger.debug(USING_MEDIAN_PRICE);
    return values;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Ehlers Filter, y = (c1x1 + c2x2 + c3x3 + c4x4 + ... + cnxn) /
    //                    (c1 + c2 + c3 + c4 + ... + cn)
    // where
    // xi is the ith input across a filter window width n
    // ci are coefficients

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      // compute coefficients and denominator
      final double[] coefficients = coefficients(i, values);
      final double denominator = denominator(coefficients);

      // compute numerator - sum product of coefficients and values
      double numerator = ZERO;
      for (int c = ZERO, v = i + priceOffset; c < coefficients.length; ++c, ++v) {
        numerator += (coefficients[c] * values[v]);
      }

      // compute filter output
      output[i] = (numerator / denominator);
    }
    cleanUp();

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected abstract double[] coefficients(final int index, final double... values);

  protected double denominator(final double... coefficients) {
    return sum(coefficients);
  }

  protected void cleanUp() {
    // do nothing
  }

}
