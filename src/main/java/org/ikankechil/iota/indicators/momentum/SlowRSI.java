/**
 * SlowRSI.java v0.1 3 August 2015 1:42:31 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Slow Relative Strength Index (SRSI) by Vitali Apirine
 * <p>
 * http://traders.com/Documentation/FEEDbk_docs/2015/07/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SlowRSI extends AbstractIndicator {

  private final int ema;

  public SlowRSI() {
    this(FOURTEEN, SIX);
  }

  public SlowRSI(final int period, final int ema) {
    super(period, TA_LIB.emaLookback(period) + TA_LIB.emaLookback(ema));
    throwExceptionIfNegative(ema);

    this.ema = ema;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final double[] emas = ema(values, ema);

    final double[] gains = new double[emas.length];
    final double[] losses = new double[gains.length];
    for (int e = ZERO, v = ema - ONE; e < emas.length; ++e, ++v) {
      final double change = values[v] - emas[e];
      if (change >= ZERO) {
        gains[e] = change;
      }
      else {
        losses[e] = -change;
      }
    }

    final double[] averageGains = ema(gains, period);
    final double[] averageLosses = ema(losses, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = RSI.rsi(averageGains[i], averageLosses[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
