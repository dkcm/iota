/**
 * SMA.java  v0.4  8 December 2014 7:10:55 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Simple Moving Average (SMA)
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class SMA extends AbstractIndicator {

  private final double inversePeriod;

  public SMA(final int period) {
    super(period, period - ONE);

    inversePeriod = ONE / (double) period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // first moving average
    int v = ZERO;
    double previous = values[v];
    while (++v < period) {
      previous += values[v];
    }
    int i = ZERO;
    output[i] = previous * inversePeriod;

    // rolling window
    double first = values[i];
    while (++i < output.length) {
      previous += values[v++] - first;  // drop earliest entry
      output[i] = previous * inversePeriod;
      first = values[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  public static final double sma(final double[] values, final int from, final int to) {
    int v = from;
    double sum = values[v];
    while (++v < to) {
      sum += values[v];
    }
    return sum / (to - from);
  }

}
