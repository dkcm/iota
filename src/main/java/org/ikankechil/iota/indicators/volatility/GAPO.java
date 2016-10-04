/**
 * GAPO.java  v0.2  8 January 2015 1:12:12 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Gopalakrishnan Range Index (GAPO) by Jayanthi Gopalakrishnan
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C01/003GOPA.pdf<br>
 * http://www.geniustrader.org/doc/GT/Indicators/GAPO.pm.html<br>
 * http://user42.tuxfamily.org/chart/manual/Gopalakrishnan-Range-Index.html<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class GAPO extends AbstractIndicator {

  private final double inverseLogP;

  public GAPO() {
    this(FIVE);
  }

  public GAPO(final int period) {
    super(period, period - ONE);

    inverseLogP = ONE / Math.log(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Gopalakarishnan Range Index = (Log(Highest High (n) - Lowest Low (n))) / Log (n)

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    for (int i = ZERO, j = i + period; i < output.length; ++i, ++j) {
      final double high = max(highs, i, j);
      final double low = min(lows, i, j);
      output[i] = Math.log(high - low) * inverseLogP;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
