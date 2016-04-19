/**
 * NVI.java v0.1 17 July 2015 3:03:49 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Negative Volume Index (NVI) by Paul Dysart and Norman Fosback
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NVI extends IndicatorWithSignalLine {

  // scaled down by 100 from original 1000 to reduce computations
  private static final int NVI_START = TEN;

  public NVI() {
    this(255);
  }

  public NVI(final int signal) {
    super(signal, TA_LIB.emaLookback(signal));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Cumulative NVI starts at 1000
    // 2. Add the Percentage Price Change to Cumulative NVI when Volume Decreases
    // 3. Cumulative NVI is Unchanged when Volume Increases
    // 4. Apply a 255-day EMA for Signals

    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();

    // compute indicator
    int i = ZERO;
    double nvi = output[i] = NVI_START;
    long pv = volumes[i]; // previous volume

    for (; ++i < output.length;) {
      final long volume = volumes[i];
      if (volume < pv) {
        // price %change
        nvi += (closes[i] / closes[i - ONE]) - ONE;
      }
      output[i] = nvi;
      pv = volume;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
