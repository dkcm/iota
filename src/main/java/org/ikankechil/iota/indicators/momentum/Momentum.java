/**
 * Momentum.java v0.2 8 December 2014 6:29:58 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Momentum
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class Momentum extends AbstractIndicator {

  public Momentum() {
    this(TEN);
  }

  public Momentum(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Momentum = Price - Price(period)

    // compute indicator
    for (int i = ZERO, v = period; i < output.length; ++i, ++v) {
      output[i] = values[v] - values[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
