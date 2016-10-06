/**
 * KST.java  v0.2  10 December 2014 10:47:07 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Know Sure Thing (KST) by Martin Pring
 *
 * <p>Time Frames<br>
 * Short-term Daily = KST(10,15,20,30,10,10,10,15,9)<br>
 * Medium-term Weekly = KST(10,13,15,20,10,13,15,20,9)<br>
 * Long-term Monthly = KST(9,12,18,24,6,6,6,9,9)<br>
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:know_sure_thing_kst<br>
 * http://en.wikipedia.org/wiki/KST_oscillator<br>
 *
 * @author Daniel Kuan
 * @version 0.2
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
    this(TEN, FIFTEEN, TWENTY, THIRTY, TEN, TEN, TEN, FIFTEEN, NINE); // short-term trend
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
    super((int) max(roc1 + (sma1 - ONE),
                    roc2 + (sma2 - ONE),
                    roc3 + (sma3 - ONE),
                    roc4 + (sma4 - ONE)) +
          (signal - ONE));
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
    signalLookback = (signal - ONE);
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
    for (int k = ZERO, i1 = smaRoc1.length - length, i2 = smaRoc2.length - length,
                       i3 = smaRoc3.length - length, i4 = smaRoc4.length - length;
         k < length;
         ++k, ++i1, ++i2, ++i3, ++i4) {
      kst[k] = smaRoc1[i1] + (smaRoc2[i2] * TWO) + (smaRoc3[i3] * THREE) + (smaRoc4[i4] * FOUR);
    }

    // compute indicator signal line
    final double[] kstSignal = sma(kst, signal);

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
