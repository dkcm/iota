/**
 * SpecialK.java  v0.2  22 December 2014 12:16:15 pm
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.HashMap;
import java.util.Map;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.momentum.ROC;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Special K by Martin Pring
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pring_s_special_k<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SpecialK extends AbstractIndicator {

  private static final int SMA_MAX = 195;
  private static final int ROC_MAX = 530;

  public SpecialK() {
    super(SMA_MAX + ROC_MAX); // = 725
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

    final double[] sma10Roc10   = smaRoc1(ohlcv,  10,  10);
    final double[] sma10Roc15   = smaRoc1(ohlcv,  10,  15);
    final double[] sma10Roc20   = smaRoc1(ohlcv,  10,  20);
    final double[] sma15Roc30   = smaRoc1(ohlcv,  15,  30);
    final double[] sma50Roc40   = smaRoc1(ohlcv,  50,  40);
    final double[] sma65Roc65   = smaRoc1(ohlcv,  65,  65);
    final double[] sma75Roc75   = smaRoc1(ohlcv,  75,  75);
    final double[] sma100Roc100 = smaRoc1(ohlcv, 100, 100);
    final double[] sma130Roc195 = smaRoc1(ohlcv, 130, 195);
    final double[] sma130Roc265 = smaRoc1(ohlcv, 130, 265);
    final double[] sma130Roc390 = smaRoc1(ohlcv, 130, 390);
    final double[] sma195Roc530 = smaRoc1(ohlcv, SMA_MAX, ROC_MAX);

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

  private static final Map<Integer, SMA> SMAS = new HashMap<>(EIGHT);
  private static final Map<Integer, ROC> ROCS = new HashMap<>(EIGHT);

  static {
    for (final int sma : new int[] { 10, 15, 50, 65, 75, 100, 130, 195 } ) {
      SMAS.put(sma, new SMA(sma));
    }
    for (final int roc : new int[] { 10, 15, 20, 30, 40, 65, 75, 100, 195, 265, 390, 530 } ) {
      ROCS.put(roc, new ROC(roc));
    }
  }

  private static final double[] smaRoc1(final OHLCVTimeSeries ohlcv, final int sma, final int roc) {
    final ROC roc2 = ROCS.get(roc);
    final SMA sma2 = SMAS.get(sma);
    final double[] smaRoc = sma2.generate(roc2.generate(ohlcv).get(ZERO)).get(ZERO).values();
    return smaRoc;
  }

}
