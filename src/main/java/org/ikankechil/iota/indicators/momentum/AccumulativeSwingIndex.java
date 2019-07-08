/**
 * AccumulativeSwingIndex.java  v0.1  21 April 2019 11:33:01 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Accumulative Swing Index by J. Welles Wilder
 *
 * <p>References:
 * <li>http://forex-indicators.net/accumulative-swing-index
 * <li>https://www.technicalindicators.net/indicators-technical-analysis/107-asi-accumulative-swing-index
 * <li>https://www.marketinout.com/technical_analysis.php?t=Accumulation_Swing_Index&id=21<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AccumulativeSwingIndex extends SwingIndex {

  public AccumulativeSwingIndex() {
    super();
  }

  public AccumulativeSwingIndex(final double limitMove) {
    super(limitMove);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // ASI = yesterday's ASI + today's SI

    super.compute(start, end, ohlcv, outBegIdx, outNBElement, output);
    int i = ZERO;
    double asi = output[i];
    while (++i < output.length) {
      final double si = output[i];
      output[i] = asi += si;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
