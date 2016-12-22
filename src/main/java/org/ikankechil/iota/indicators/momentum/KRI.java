/**
 * KRI.java  0.1  22 December 2016 4:59:18 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kairi Relative Index (KRI)
 *
 * <p>http://fxcodebase.com/wiki/index.php/Kairi_Relative_Index_(KRI)<br>
 * http://www.investopedia.com/articles/forex/09/kairi-relative-strength-index.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KRI extends AbstractIndicator {

  public KRI() {
    this(FOURTEEN);
  }

  public KRI(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // KRI = (Price - SMA) / SMA * 100%

    final double[] sma = sma(values, period);

    // compute indicator
    for (int i = ZERO, j = i + lookback; i < output.length; ++i, ++j) {
      output[i] = ((values[j] / sma[i]) - ONE) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
