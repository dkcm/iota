/**
 * APO.java  v0.1  19 December 2014 5:57:17 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Absolute Price Oscillator (APO)
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class APO extends IndicatorWithSignalLine {

  private final int fast;
  private final int slow;

  public APO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public APO(final int fast, final int slow, final int signal) {
    super(signal, TA_LIB.apoLookback(fast, slow, MAType.Ema) + TA_LIB.emaLookback(signal));
    throwExceptionIfNegative(fast, slow);

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
    return TA_LIB.apo(start,
                      end,
                      values,
                      fast,
                      slow,
                      MAType.Ema,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
