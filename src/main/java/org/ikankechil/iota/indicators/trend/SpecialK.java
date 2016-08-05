/**
 * SpecialK.java  v0.1  22 December 2014 12:16:15 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Martin Pring's Special K
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pring_s_special_k
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SpecialK extends AbstractIndicator {

  private static final int SMA = 195;
  private static final int ROC = 530;

  public SpecialK() {
    super(SMA + ROC); // = 725
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Special K = 10 Period Simple Moving Average of ROC(10) * 1
    //           + 10 Period Simple Moving Average of ROC(15) * 2
    //           + 10 Period Simple Moving Average of ROC(20) * 3
    //           + 15 Period Simple Moving Average of ROC(30) * 4
    //           + 50 Period Simple Moving Average of ROC(40) * 1
    //           + 65 Period Simple Moving Average of ROC(65) * 2
    //           + 75 Period Simple Moving Average of ROC(75) * 3
    //           +100 Period Simple Moving Average of ROC(100)* 4
    //           +130 Period Simple Moving Average of ROC(195)* 1
    //           +130 Period Simple Moving Average of ROC(265)* 2
    //           +130 Period Simple Moving Average of ROC(390)* 3
    //           +195 Period Simple Moving Average of ROC(530)* 4

    final double[] sma10Roc10   = smaRoc(ohlcv,  10,  10);
    final double[] sma10Roc15   = smaRoc(ohlcv,  10,  15);
    final double[] sma10Roc20   = smaRoc(ohlcv,  10,  20);
    final double[] sma15Roc30   = smaRoc(ohlcv,  15,  30);
    final double[] sma50Roc40   = smaRoc(ohlcv,  50,  40);
    final double[] sma65Roc65   = smaRoc(ohlcv,  65,  65);
    final double[] sma75Roc75   = smaRoc(ohlcv,  75,  75);
    final double[] sma100Roc100 = smaRoc(ohlcv, 100, 100);
    final double[] sma130Roc195 = smaRoc(ohlcv, 130, 195);
    final double[] sma130Roc265 = smaRoc(ohlcv, 130, 265);
    final double[] sma130Roc390 = smaRoc(ohlcv, 130, 390);
    final double[] sma195Roc530 = smaRoc(ohlcv, SMA, ROC);

    final int length = output.length;
    for (int k = 0, i1 =   sma10Roc10.length - length,  i2 =   sma10Roc15.length - length,  i3 =   sma10Roc20.length - length,  i4 =   sma15Roc30.length - length,
                    i5 =   sma50Roc40.length - length,  i6 =   sma65Roc65.length - length,  i7 =   sma75Roc75.length - length,  i8 = sma100Roc100.length - length,
                    i9 = sma130Roc195.length - length, i10 = sma130Roc265.length - length, i11 = sma130Roc390.length - length, i12 = sma195Roc530.length - length;
         k < length;
         ++k, ++i1, ++i2, ++i3, ++i4,
              ++i5, ++i6, ++i7, ++i8,
              ++i9, ++i10, ++i11, ++i12) {
      output[k] =   sma10Roc10[i1] + (  sma10Roc15[i2]  * TWO) + (  sma10Roc20[i3]  * THREE) + (  sma15Roc30[i4]  * FOUR) +
                    sma50Roc40[i5] + (  sma65Roc65[i6]  * TWO) + (  sma75Roc75[i7]  * THREE) + (sma100Roc100[i8]  * FOUR) +
                  sma130Roc195[i9] + (sma130Roc265[i10] * TWO) + (sma130Roc390[i11] * THREE) + (sma195Roc530[i12] * FOUR);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
