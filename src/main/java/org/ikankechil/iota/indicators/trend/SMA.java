/**
 * SMA.java  v0.3  8 December 2014 7:10:55 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
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
 * @version 0.3
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

}
