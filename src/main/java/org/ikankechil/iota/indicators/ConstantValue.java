/**
 * ConstantValue.java  v0.1  7 April 2017 1:28:33 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ConstantValue extends AbstractIndicator {

  private final double constant;

  public ConstantValue(final double constant) {
    super(ZERO);

    this.constant = constant;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    Arrays.fill(output, constant);
    return RetCode.Success;
  }

}
