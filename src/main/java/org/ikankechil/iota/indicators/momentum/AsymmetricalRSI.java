/**
 * AsymmetricalRSI.java  v0.1  23 July 2015 12:29:45 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Asymmetrical Relative Strength Index (ARSI) by Sylvain Vervoort
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V26/C10/181VERV.pdf
 * http://traders.com/Documentation/FEEDbk_docs/2008/10/TradersTips/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class AsymmetricalRSI extends RSI {

  public AsymmetricalRSI() {
    this(FOURTEEN);
  }

  public AsymmetricalRSI(final int period) {
    super(period);
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
    //
    // Formula:
    // Period:=Input("ARSI Time Period ->",1,100,14);
    // UpCount:=Sum(If(ROC(C,1,$)>=0,1,0),Period);
    // DnCount:=Period-UpCount;
    // UpMove:=ExtFml("Forum.MOV",If(ROC(C,1,$)>=0,ROC(C,1,$),0),UpCount*2-1,E);
    // DnMove:=ExtFml("Forum.MOV",If(ROC(C,1,$)<0,Abs(ROC(C,1,$)),0),DnCount*2-1,E);
    // RS:=UpMove/(DnMove+0.0001);
    // 100-(100/(1+RS));

    // compute average gain and loss (first value)
    double averageGain = ZERO;
    double averageLoss = ZERO;
    int gains = ZERO;
    int v = ZERO;
    double previous = values[v];
    for (; ++v <= period; ) {
      final double current = values[v];
      final double change = current - previous;
      if (change >= ZERO) {
        averageGain += change;
        ++gains;
      }
      else {
        averageLoss -= change;
      }
      previous = current;
    }
    averageGain /= gains;
    averageLoss /= (period - gains);

    // compute indicator (first value)
    int i = ZERO;
    double rsi = rsi(averageGain, averageLoss);
    output[i] = rsi;

    // compute average gain and loss (subsequent values)
    for (; v < values.length; ++v) {
      final double current = values[v];
      final double change = current - previous;
      double currentGain = ZERO;
      double currentLoss = ZERO;
      if (change >= ZERO) {
        currentGain = change;
        ++gains;
        if (gains >= period) {
          gains = period;
        }
      }
      else {
        currentLoss = -change;
        --gains;
        if (gains < ZERO) {
          gains = ZERO;
        }
      }

      final int losses = period - gains;
      averageGain = (averageGain * (gains * TWO - ONE) + currentGain) / gains;
      averageLoss = (averageLoss * (losses * TWO - ONE) + currentLoss) / losses;

      previous = current;

      // compute indicator (subsequent values)
      rsi = rsi(averageGain, averageLoss);
      output[++i] = rsi;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}

