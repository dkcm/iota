/**
 * MaximumPriceIndex.java  v0.2  27 January 2015 1:00:17 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Maximum Price Index
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MaximumPriceIndex extends AbstractIndicator {

  public MaximumPriceIndex(final int period) {
    super(period, (period - ONE));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // find first maximum
    int from = ZERO;
    int maxIndex = maxIndex(from, from + period, values);
    double max = values[maxIndex];

    // compute indicator
    output[from] = maxIndex;
    for (int current = from + period; ++from < output.length; ++current) {
      // incumbent no longer in window
      if (maxIndex < from) {
        maxIndex = maxIndex(from, from + period, values);
        max = values[maxIndex];
      }
      // incumbent < current
      else if (max < values[current]) {
        max = values[current];
        maxIndex = current;
      }

      output[from] = maxIndex;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  /**
   *
   *
   * @param from inclusive
   * @param to exclusive
   * @param values
   * @return index
   */
  public static final int maxIndex(final int from, final int to, final double... values) {
    int maxIndex = from;
    double max = values[maxIndex];
    for (int i = maxIndex + ONE; i < to; ++i) {
      final double value = values[i];
      if (value > max) {
        max = value;
        maxIndex = i;
      }
    }
    return maxIndex;
  }

}
