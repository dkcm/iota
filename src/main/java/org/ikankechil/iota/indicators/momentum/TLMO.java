/**
 * TLMO.java  v0.1  4 January 2017 1:29:22 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Tick Line Momentum Oscillator (TLMO) by Daniel E. Downing
 *
 * <p>http://www.meta-formula.com/Metastock-Formulas-T.html#Tick_Line_Momentum_Oscillator_<br>
 * http://exceltechnical.web.fc2.com/tlmo.html<br>
 * https://www.earnforex.com/books/en/beginner-forex-trading/the-nyse-tick-index-and-candlesticks.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TLMO extends AbstractIndicator {

  private final int m;
  private final int s;

  public TLMO() {
    this(TEN, FIVE, FIVE);
  }

  public TLMO(final int period, final int momentum, final int smooth) {
    super(period, period + momentum + smooth - TWO);
    throwExceptionIfNegative(momentum, smooth);

    m = momentum;
    s = smooth;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // TLMO = Mov( ROC( Cum( If( C ,> ,Ref( Mov(C ,10 ,E ) ,-1 ) ,+1 ,If( C ,< ,Ref( Mov( C ,10 ,E ) ,-1 ) ,- 1 ,0 ) ) ) ,5 ,$ ) ,5 ,E )

    final int[] cumulativeTicks = accumulateTicks(values);
    final double[] momentum = computeMomentum(cumulativeTicks);

    // compute indicator
    final double[] tlmo = ema(momentum, s);
    System.arraycopy(tlmo, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final int[] accumulateTicks(final double[] values) {
    final double[] emas = ema(values, period);

    final int[] cumulativeTicks = new int[emas.length];
    int i = ZERO;
    int pc = cumulativeTicks[i] = ZERO; // previous cumulative

    for (int j = values.length - emas.length + ONE; ++i < cumulativeTicks.length; ++j) {
      final double ema = emas[i];
      final double value = values[j];

      pc = cumulativeTicks[i] = (value > ema) ? ++pc :
                                (value < ema) ? --pc :
                                                  pc;
    }

    return cumulativeTicks;
  }

  private final double[] computeMomentum(final int[] cumulativeTicks) {
    final double[] momentum = new double[cumulativeTicks.length - m];
    for (int i = ZERO, j = m; i < momentum.length; ++i, ++j) {
      momentum[i] = cumulativeTicks[j] - cumulativeTicks[i];
    }
    return momentum;
  }

}
