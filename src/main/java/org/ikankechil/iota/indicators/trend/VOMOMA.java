/**
 * VOMOMA.java  v0.1  16 July 2015 12:25:20 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume-adjusted and Move-adjusted Moving Average (VOMOMA) by Stephan Bisse
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C02/029BISS.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C03/059BISS.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VOMOMA extends AbstractIndicator {

  private final VOMA voma;
  private final MOMA moma;

  public VOMOMA() {
    this(FOUR);
  }

  public VOMOMA(final int period) {
    super(period, period);

    voma = new VOMA(period);
    moma = new MOMA(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // VOMOMA = (VOMA + MOMA) / 2

    final int size = ohlcv.size();

    final double[] vomas = new double[size - voma.lookback()];
    RetCode outcome = voma.compute(start,
                                   end,
                                   ohlcv,
                                   outBegIdx,
                                   outNBElement,
                                   vomas);
    throwExceptionIfBad(outcome, ohlcv);

    final double[] momas = new double[size - moma.lookback()];
    outcome = moma.compute(start,
                           end,
                           ohlcv,
                           outBegIdx,
                           outNBElement,
                           momas);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO; i < output.length;) {
      output[i] = (momas[i] + vomas[++i]) * HALF;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
