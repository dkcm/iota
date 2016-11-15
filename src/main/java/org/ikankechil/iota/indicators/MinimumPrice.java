/**
 * MinimumPrice.java  v0.2  27 January 2015 12:54:40 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minimum Price
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MinimumPrice extends AbstractIndicator {

  public MinimumPrice(final int period) {
    super(period, (period - ONE));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // find first minimum
    int from = ZERO;
    int minIndex = minIndex(from, from + period, values);
    double min = values[minIndex];

    // compute indicator
    output[from] = min;
    for (int current = from + period; ++from < output.length; ++current) {
      // incumbent no longer in window
      if (minIndex < from) {
        minIndex = minIndex(from, from + period, values);
        min = values[minIndex];
      }
      // incumbent > current
      else if (min > values[current]) {
        min = values[current];
        minIndex = current;
      }

      output[from] = min;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final int minIndex(final int from, final int to, final double... values) {
    int minIndex = from;
    double min = values[minIndex];
    for (int i = minIndex + ONE; i < to; ++i) {
      final double value = values[i];
      if (value < min) {
        min = value;
        minIndex = i;
      }
    }
    return minIndex;
  }

}
