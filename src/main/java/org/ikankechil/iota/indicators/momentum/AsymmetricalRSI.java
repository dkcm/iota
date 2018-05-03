/**
 * AsymmetricalRSI.java  v0.3  23 July 2015 12:29:45 am
 *
 * Copyright © 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.trend.EMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Asymmetrical Relative Strength Index (ARSI) by Sylvain Vervoort
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2008/10/TradersTips/TradersTips.html<br>
 * <li>http://exceltechnical.web.fc2.com/p-asymrsi.html<br>
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V26/C10/181VERV.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class AsymmetricalRSI extends RSI {

  private final double[] alphas;

  public AsymmetricalRSI() {
    this(FOURTEEN);
  }

  public AsymmetricalRSI(final int period) {
    super(period);

    // initialise
    alphas = new double[period + ONE];
    alphas[ONE] = ONE;
    for (int i = TWO; i < alphas.length; ++i) {
      alphas[i] = ONE / (double) i;
    }
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // RSI = 100 - (100 / (1 + RS))
    // RS = Average Gain / Average Loss
    //
    // Formula:
    // Period:=Input("ARSI Time Period ->",1,100,14);
    // UpCount:=Sum(If(ROC(C,1,$)>=0,1,0),Period);
    // DnCount:=Period-UpCount;
    // UpMove:=ExtFml("Forum.MOV",If(ROC(C,1,$)>=0,ROC(C,1,$),0),UpCount*2-1,E);
    // DnMove:=ExtFml("Forum.MOV",If(ROC(C,1,$)<0,Abs(ROC(C,1,$)),0),DnCount*2-1,E);
    // RS:=UpMove/(DnMove+0.0001);
    // 100-(100/(1+RS));

    // compute sum gain and loss (first value)
    double sumGain = ZERO;
    double sumLoss = ZERO;
    final boolean[] gains = new boolean[values.length - ONE];
    int gain = ZERO;
    int v = ZERO;
    double previous = values[v];
    for (; ++v <= period; ) {
      final double current = values[v];
      final double change = current - previous;
      if (change >= ZERO) {
        sumGain += change;
        ++gain;
        gains[v - ONE] = true;
      }
      else {
        sumLoss -= change;
      }
      previous = current;
    }
    int loss = period - gain;

    // compute indicator (first value)
    int i = ZERO;
    double rsi = computeRSI(sumGain, sumLoss);
    output[i] = rsi;

    // compute sum gain and loss (subsequent values)
    for (; v < values.length; ++v) {
      final double current = values[v];
      final double change = current - previous;

      if (change >= ZERO) { // gain
        if (!gains[i]) {
          ++gain;
          --loss;
        }
        sumGain = smooth(gain, change, sumGain);
        sumLoss = smooth(loss, ZERO, sumLoss);
        gains[v - ONE] = true;
      }
      else {                // loss
        if (gains[i]) {
          --gain;
          ++loss;
        }
        sumGain = smooth(gain, ZERO, sumGain);
        sumLoss = smooth(loss, -change, sumLoss);
      }

      previous = current;

      // compute indicator (subsequent values)
      output[++i] = rsi = computeRSI(sumGain, sumLoss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private double smooth(final int changes,
                        final double change,
                        final double previousSum) {
    return (changes > ZERO) ? EMA.ema(alphas[changes], change, previousSum)
                            : previousSum;
  }

}
