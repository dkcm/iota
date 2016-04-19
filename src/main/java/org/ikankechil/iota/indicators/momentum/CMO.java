/**
 * CMO.java v0.1 8 December 2014 10:15:02 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chande Momentum Oscillator (CMO)
 * <p>
 *
 * @author Daniel Kuan
 * @version
 */
public class CMO extends AbstractIndicator {

  public CMO() {
    this(FOURTEEN);
  }

  public CMO(final int period) {
    super(period, TA_LIB.cmoLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.cmo(start,
                      end,
                      values,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
