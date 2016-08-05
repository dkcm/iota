/**
 * ZeroLagEMA.java  v0.1  20 July 2015 11:18:26 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Zero-Lag Exponential Moving Average (EMA) by Peter Martin
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagEMA extends AbstractIndicator {

  public ZeroLagEMA() {
    this(TEN);
  }

  public ZeroLagEMA(final int period) {
    super(period, (period - ONE) << ONE ); // lookback = (period - 1) * 2
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Period:= Input("What Period",1,250,10);
    // EMA1:= Mov(CLOSE,Period,E);
    // EMA2:= Mov(EMA1,Period,E);
    // Difference:= EMA1 - EMA2;
    // ZeroLagEMA:= EMA1 + Difference;

    final double[] ema1 = ema(values, period);
    final double[] ema2 = ema(ema1, period);

    for (int i = ZERO, j = period - ONE; i < output.length; ++i, ++j) {
      output[i] = (ema1[j] * TWO) - ema2[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}

