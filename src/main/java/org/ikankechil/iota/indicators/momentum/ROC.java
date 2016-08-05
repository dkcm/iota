/**
 * ROC.java  v0.2 10 December 2014 9:17:51 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Rate of change (ROC)
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:rate_of_change_roc_and_momentum
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ROC extends AbstractIndicator {

  public ROC() {
    this(TEN);
  }

  public ROC(final int period) {
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
    // ROC = [(Close - Close n periods ago) / (Close n periods ago)] * 100

    // compute indicator
    for (int i = ZERO, v = period; i < output.length; ++i, ++v) {
      output[i] = (values[v] / values[i] - ONE) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
