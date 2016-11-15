/**
 * MaximumPrice.java  v0.3  27 January 2015 12:52:10 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import static org.ikankechil.iota.indicators.MaximumPriceIndex.*;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Maximum Price
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MaximumPrice extends AbstractIndicator {

  public MaximumPrice(final int period) {
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
    output[from] = max;
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

      output[from] = max;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
