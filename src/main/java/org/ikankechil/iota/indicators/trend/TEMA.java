/**
 * TEMA.java	v0.1	7 January 2015 10:41:58 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Triple Exponential Moving Average (TEMA).
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TEMA extends AbstractIndicator {

  public TEMA(final int period) {
    super(period, TA_LIB.temaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.tema(start,
                       end,
                       values,
                       period,
                       outBegIdx,
                       outNBElement,
                       output);
  }

}
