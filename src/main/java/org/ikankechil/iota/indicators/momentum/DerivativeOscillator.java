/**
 * DerivativeOscillator.java  v0.1  4 July 2017 9:55:07 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Derivative Oscillator by Constance Brown
 *
 * <p>References:
 * <li>http://aeroinvest.com/derivative.pdf<br>
 * <li>http://www2.wealth-lab.com/WL5wiki/DerivativeOscillator.ashx<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DerivativeOscillator extends AbstractIndicator {

  private final Indicator rsi;
  private final int       ema1;
  private final int       ema2;
  private final int       sma;

  public DerivativeOscillator() {
    this(FOURTEEN, FIVE, THREE, NINE);
  }

  public DerivativeOscillator(final int rsi, final int ema1, final int ema2, final int sma) {
    super(rsi, rsi + ema1 + ema2 + sma - THREE);
    throwExceptionIfNegative(ema1, ema2, sma);

    this.rsi = new RSI(rsi);
    this.ema1 = ema1;
    this.ema2 = ema2;
    this.sma = sma;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Calculate EMA of RSI
    // 2. EMA of step 1
    // 3. SMA of step 2
    // 4. Derivative Oscillator = Difference between steps 2 and 3

    final double[] rsis = rsi.generate(ohlcv).get(ZERO).values();
    final double[] ema1s = ema(rsis, ema1);
    final double[] ema2s = ema(ema1s, ema2);
    final double[] smas = sma(ema2s, sma);

    // compute indicator
    for (int i = ZERO, j = i + sma - ONE; i < output.length; ++i, ++j) {
      output[i] = ema2s[j] - smas[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
