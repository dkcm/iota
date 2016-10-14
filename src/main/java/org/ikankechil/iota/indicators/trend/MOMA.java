/**
 * MOMA.java  v0.1  16 July 2015 12:26:46 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Move-adjusted Moving Average (MOMA) by Stephan Bisse
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C02/029BISS.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C03/059BISS.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MOMA extends AbstractIndicator {

  public MOMA() {
    this(FOUR);
  }

  public MOMA(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Sum up all absolute changes between closes in the lookback period
    // 2. Multiply each close by the relative absolute move
    // 3. Sum up all move-adjusted closes

    // compute absolute sum of changes and move-adjusted closes
    final double[] absChanges = new double[ohlcv.size() - ONE];
    final double[] moveAdjustedCloses = new double[absChanges.length];
    int c = ZERO;
    double yesterday = ohlcv.close(c);
    for (int i = ZERO; i < absChanges.length; ++i) {
      final double today = ohlcv.close(++c);
      final double absChange = absChanges[i] = Math.abs(today - yesterday);
      moveAdjustedCloses[i] = today * absChange;

      // shift forward in time;
      yesterday = today;
    }

    // compute sums
    final double[] absSumChanges = sum(period, absChanges);
    final double[] sumMoveAdjustedCloses = sum(period, moveAdjustedCloses);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = sumMoveAdjustedCloses[i] / absSumChanges[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
