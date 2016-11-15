/**
 * MinimumPrice.java  v0.3  27 January 2015 12:54:40 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static org.ikankechil.iota.indicators.MinimumPriceIndex.*;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Minimum Price
 *
 *
 * @author Daniel Kuan
 * @version 0.3
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

}
