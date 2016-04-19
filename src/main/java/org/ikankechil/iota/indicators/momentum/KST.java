/**
 * KST.java v0.1 10 December 2014 10:47:07 AM
 *
 * Copyright � 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Know Sure Thing (KST)
 *
 * Time Frames
 * Short-term Daily = KST(10,15,20,30,10,10,10,15,9)
 * Medium-term Weekly = KST(10,13,15,20,10,13,15,20,9)
 * Long-term Monthly = KST(9,12,18,24,6,6,6,9,9)
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:know_sure_thing_kst
 * http://en.wikipedia.org/wiki/KST_oscillator
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KST extends AbstractIndicator {

  // ROC periods
  private final int           roc1;
  private final int           roc2;
  private final int           roc3;
  private final int           roc4;

  // SMA periods
  private final int           sma1;
  private final int           sma2;
  private final int           sma3;
  private final int           sma4;

  // Signal-line period
  private final int           signal;
  private final int           signalLookback;

  private static final String KST_SIGNAL = "KST Signal";

  public KST() {
    this(10, 15, 20, 30, 10, 10, 10, 15, 9); // short-term trend
  }

  public KST(final int roc1,
             final int roc2,
             final int roc3,
             final int roc4,
             final int sma1,
             final int sma2,
             final int sma3,
             final int sma4,
             final int signal) {
    super(Math.max(Math.max(TA_LIB.rocLookback(roc1) + TA_LIB.smaLookback(sma1),
                            TA_LIB.rocLookback(roc2) + TA_LIB.smaLookback(sma2)),
                   Math.max(TA_LIB.rocLookback(roc3) + TA_LIB.smaLookback(sma3),
                            TA_LIB.rocLookback(roc4) + TA_LIB.smaLookback(sma4))) +
          TA_LIB.smaLookback(signal));
    throwExceptionIfNegative(roc1,
                             roc2,
                             roc3,
                             roc4,
                             sma1,
                             sma2,
                             sma3,
                             sma4,
                             signal);

    // ROC periods
    this.roc1 = roc1;
    this.roc2 = roc2;
    this.roc3 = roc3;
    this.roc4 = roc4;

    // SMA periods
    this.sma1 = sma1;
    this.sma2 = sma2;
    this.sma3 = sma3;
    this.sma4 = sma4;

    // Signal-line period
    this.signal = signal;
    signalLookback = TA_LIB.smaLookback(signal);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // RCMA1 = 10-Period SMA of 10-Period Rate-of-Change
    // RCMA2 = 10-Period SMA of 15-Period Rate-of-Change
    // RCMA3 = 10-Period SMA of 20-Period Rate-of-Change
    // RCMA4 = 15-Period SMA of 30-Period Rate-of-Change
    //
    // KST = (RCMA1 x 1) + (RCMA2 x 2) + (RCMA3 x 3) + (RCMA4 x 4)
    //
    // Signal Line = 9-period SMA of KST

    throwExceptionIfShort(ohlcv);

    final double[] smaRoc1 = smaRoc(ohlcv, sma1, roc1);
    final double[] smaRoc2 = smaRoc(ohlcv, sma2, roc2);
    final double[] smaRoc3 = smaRoc(ohlcv, sma3, roc3);
    final double[] smaRoc4 = smaRoc(ohlcv, sma4, roc4);

    return kst(ohlcv, smaRoc1, smaRoc2, smaRoc3, smaRoc4);
  }

  private List<TimeSeries> kst(final OHLCVTimeSeries ohlcv,
                               final double[] smaRoc1,
                               final double[] smaRoc2,
                               final double[] smaRoc3,
                               final double[] smaRoc4) {
    final int size = ohlcv.size();

    // compute indicator
    final double[] kst = new double[(size - lookback) + signalLookback];
    final int length = kst.length;
    for (int k = 0, i1 = smaRoc1.length - length, i2 = smaRoc2.length - length,
                    i3 = smaRoc3.length - length, i4 = smaRoc4.length - length;
         k < length;
         ++k, ++i1, ++i2, ++i3, ++i4) {
      kst[k] = smaRoc1[i1] + (smaRoc2[i2] * 2) + (smaRoc3[i3] * 3) + (smaRoc4[i4] * 4);
//      kst[k] = smaRoc1[i1] +
//              (smaRoc2[i2] + smaRoc2[i2]) +
//              (smaRoc3[i3] + smaRoc3[i3] + smaRoc3[i3]) +
//              (smaRoc4[i4] + smaRoc4[i4] + smaRoc4[i4] + smaRoc4[i4]);
    }

    // compute indicator signal line
    final double[] kstSignal = sma(kst, signal);
//    final MInteger outBegIdx = new MInteger();
//    final MInteger outNBElement = new MInteger();
//
//    final double[] kstSignal = new double[length - signalLookback];
//
//    final RetCode outcome = TA_LIB.sma(ZERO,
//                                       length - ONE,
//                                       kst,
//                                       signal,
//                                       outBegIdx,
//                                       outNBElement,
//                                       kstSignal);
//    throwExceptionIfBad(outcome, ohlcv); // 4719ms

    // build output
    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(kst,
                                                           signalLookback,
                                                           length)),
                         new TimeSeries(KST_SIGNAL,
                                        dates,
                                        kstSignal));
  }

}
