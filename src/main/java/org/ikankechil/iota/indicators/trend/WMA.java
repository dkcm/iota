/**
 * WMA.java  v0.1 8 December 2014 8:37:37 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Weighted Moving Average (WMA)
 *
 * @author Daniel Kuan
 * @version
 */
public class WMA extends AbstractIndicator {

  public WMA(final int period) {
    super(period, TA_LIB.wmaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.wma(start,
                      end,
                      values,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
