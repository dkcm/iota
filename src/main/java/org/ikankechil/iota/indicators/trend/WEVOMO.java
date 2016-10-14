/**
 * WEVOMO.java  v0.1  9 October 2016 11:08:22 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Weight + Volume + Move-Adjusted Moving Average (WEVOMO) by Stephan Bisse
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C04/081BISS.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WEVOMO extends AbstractIndicator {

  private final WMA  wma;
  private final VOMA voma;
  private final MOMA moma;

  public WEVOMO() {
    this(FOUR);
  }

  public WEVOMO(final int period) {
    super(period, period);

    wma = new WMA(period);
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
    // WEVOMO = (WMA + VOMA + MOMA) / 3

    final int size = ohlcv.size();

    final double[] wmas = new double[size - wma.lookback()];
    RetCode outcome = wma.compute(start,
                                  end,
                                  ohlcv.closes(),
                                  outBegIdx,
                                  outNBElement,
                                  wmas);
    throwExceptionIfBad(outcome, ohlcv);

    final double[] vomas = new double[size - voma.lookback()];
    outcome = voma.compute(start,
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
      output[i] = (momas[i] + vomas[++i] + wmas[i]) * THIRD;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
