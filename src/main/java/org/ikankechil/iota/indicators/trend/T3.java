/**
 * T3.java	v0.1	7 January 2015 9:56:35 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * T3
 * <p>
 * http://www.fmlabs.com/reference/default.htm?url=T3.htm
 * http://www.forexfactory.com/attachment.php?attachmentid=709307&d=1306688278
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class T3 extends AbstractIndicator {

  private final double volumeFactor;

  public T3(final int period) {
    this(period, 0.7);
  }

  public T3(final int period, final double volumeFactor) {
    super(period, TA_LIB.t3Lookback(period, volumeFactor));
    throwExceptionIfNegative(volumeFactor);

    this.volumeFactor = volumeFactor;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.t3(start,
                     end,
                     values,
                     period,
                     volumeFactor,
                     outBegIdx,
                     outNBElement,
                     output);
  }

}
