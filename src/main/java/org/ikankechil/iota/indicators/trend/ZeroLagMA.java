/**
 * ZeroLagMA.java  0.1  8 July 2017 3:28:01 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Abstract superclass of Zero-Lag Moving Averages
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class ZeroLagMA extends AbstractIndicator {

  private final Indicator ma; // moving average

  /**
   *
   *
   * @param ma Moving average
   */
  public ZeroLagMA(final Indicator ma) {
    super(ma.lookback() + ONE, ma.lookback() * TWO);

    this.ma = ma;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // MA1:= MA(CLOSE, period);
    // MA2:= MA(MA1, period);
    // ZeroLag MA:= MA1 + (MA1 - MA2);

    final TimeSeries ma1 = ma.generate(new TimeSeries(EMPTY, new String[values.length], values)).get(ZERO);
    final TimeSeries ma2 = ma.generate(ma1).get(ZERO);

    for (int i = ZERO, j = i + ma.lookback(); i < output.length; ++i, ++j) {
      output[i] = (ma1.value(j) * TWO) - ma2.value(i);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
