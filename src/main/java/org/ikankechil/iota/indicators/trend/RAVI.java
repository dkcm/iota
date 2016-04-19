/**
 * RAVI.java v0.1 8 January 2015 3:00:34 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Range Action Verification Index (RAVI)
 * <p>
 * http://www.tradesignalonline.com/en/lexicon/view.aspx?id=RAVI+Indicator+(RAVI)
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RAVI extends AbstractIndicator {

  private final int fast;
  private final int slow;

  public RAVI() {
    this(SEVEN, 65);
  }

  public RAVI(final int fast, final int slow) {
    super(TA_LIB.smaLookback(slow));
    throwExceptionIfNegative(fast, slow);
    if (fast > slow) {
      throw new IllegalArgumentException(String.format("fast > slow: %d > %d",
                                                       fast,
                                                       slow));
    }

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // RAVI = (MA1 - MA2) / MA2 * 100

    // compute SMAs
    final double[] fasts = sma(values, fast);
    final double[] slows = sma(values, slow);

    // compute indicator
    for (int i = ZERO, j = fasts.length - slows.length; i < output.length; ++i, ++j) {
      output[i] = ((fasts[j] / slows[i]) - ONE) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
