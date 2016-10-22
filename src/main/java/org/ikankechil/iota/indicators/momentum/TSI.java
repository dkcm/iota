/**
 * TSI.java  v0.1  19 December 2014 12:10:13 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * True Strength Index (TSI) by William Blau
 *
 * <p>https://community.tradestation.com/discussions/data/2002125162918_at-tsi%20jan2002.pdf<br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:true_strength_index<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TSI extends AbstractIndicator {

  private final int smoothPeriod;

  public TSI() {
    this(TWENTY_FIVE, THIRTEEN);
  }

  public TSI(final int period, final int smoothPeriod) {
    super(period, (period + smoothPeriod - ONE));
    throwExceptionIfNegative(smoothPeriod);

    this.smoothPeriod = smoothPeriod;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula
    // TSI(close,r,s) = 100 * EMA(EMA(mtm,r),s) / EMA(EMA(|mtm|,r),s)
    //     where
    //     mtm = closetoday – closeyesterday
    //     EMA(mtm,r) = exponential moving average of mtm with period length = r
    //     EMA(EMA(mtm,r),s) = exponential moving average of EMA(mtm,r) with period length = s
    //     |mtm| = absolute value of mtm
    //     r = 25
    //     s = 13

    // compute momentum and |momentum|
    final double[] momentum = new double[values.length - ONE];
    final double[] absMomentum = new double[momentum.length];
    double today;
    double yesterday = values[ZERO];
    for (int i = ZERO; i < values.length - ONE; ++i) {
      today = values[i + ONE];
      momentum[i] = today - yesterday;
      absMomentum[i] = Math.abs(momentum[i]);
      yesterday = today;  // only need to access closes[] once
    }

    // compute EMA(momentum)
    final double[] emaMomentum = ema(momentum, period);

    // compute EMA(|momentum|)
    final double[] emaAbsMomentum = ema(absMomentum, period);

    // compute EMA(EMA(momentum))
    final double[] emaEmaMomentum = ema(emaMomentum, smoothPeriod);

    // compute EMA(EMA(|momentum|))
    final double[] emaEmaAbsMomentum = ema(emaAbsMomentum, smoothPeriod);

    // compute TSI
    // TSI(close,r,s) = 100 * EMA(EMA(mtm,r),s) / EMA(EMA(|mtm|,r),s)
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = HUNDRED_PERCENT * emaEmaMomentum[i] / emaEmaAbsMomentum[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
