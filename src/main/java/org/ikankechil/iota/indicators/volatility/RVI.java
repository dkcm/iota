/**
 * RVI.java	v0.1	21 January 2015 1:15:32 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Relative Volatility Index (RVI) by Donald Dorsey
 * <p>
 * http://user42.tuxfamily.org/chart/manual/Relative-Volatility-Index.html
 * http://etfhq.com/blog/2011/02/16/relative-volatility-index/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class RVI extends AbstractIndicator {

  private final int    stdDev;
  private final int    stdDevLookback;
  private final double multiplier;

  public RVI() {
    this(FOURTEEN, TEN);
  }

  public RVI(final int period, final int stdDev) {
    super(period, period + TA_LIB.stdDevLookback(stdDev, ONE) + ONE);
    throwExceptionIfNegative(stdDev);

    this.stdDev = stdDev;
    stdDevLookback = TA_LIB.stdDevLookback(stdDev, ONE);
    multiplier = (period - ONE) / period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // TODO implement for OHLC
    return null;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // RVI = 100 * U / (U + D)
    // Where:
    // U = Wilder’s Smoothing,N of USD
    // D = Wilder’s Smoothing,N of DSD
    // USD = If close > close(1) then SD,S else 0
    // DSD = If close < close(1) then SD,S else 0
    // S = User selected period for the Standard Deviation of the close (Dorsey suggested 10).
    // N = User selected smoothing period (Dorsey suggested 14)

    final int size = values.length;

    // compute standard deviation
    final double[] sd = new double[size - stdDevLookback];
    final RetCode outcome = TA_LIB.stdDev(start,
                                          end,
                                          values,
                                          stdDev,
                                          ONE,
                                          outBegIdx,
                                          outNBElement,
                                          sd);
    throwExceptionIfBad(outcome, null);

    // compute ups and downs
    final double[] up = new double[sd.length - ONE];
    final double[] down = new double[up.length];
    for (int i = ZERO, j = size - up.length, today = ONE, yesterday = today - ONE;
         i < up.length;
         ++i, ++j, ++today, ++yesterday) {
      if (values[today] >= values[yesterday]) {
        up[i] = sd[j];
        down[i] = ZERO;
      }
      else {
        up[i] = ZERO;
        down[i] = sd[j];
      }
    }

    // compute indicator
    double sup = ZERO;
    double sdown = ZERO;
    for (int i = ZERO; i < period; ++i) {
      sup += up[i];
      sdown += down[i];
    }
    for (int i = ZERO, j = period; i < output.length; ++i, ++j) {
      output[i] = (100 * sup) / (sup + sdown);

      // smooth ups
      sup *= multiplier;
      sup += up[j];
      // smooth downs
      sdown *= multiplier;
      sdown += down[j];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
