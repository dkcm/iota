/**
 * MultiVoteOBV.java	v0.1	17 July 2015 1:31:01 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Multi-Vote On Balance Volume by Barry M. McVicar
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V11/C06/TRADERS.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MultiVoteOBV extends AbstractIndicator {

  public MultiVoteOBV() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();
    final long[] volumes = ohlcv.volumes();

    // compute indicator
    int i = ZERO;
    double ph = highs[i];
    double pl = lows[i];
    double pc = closes[i];
    double pmvobv = output[i] = volumes[i];

    for (; ++i < output.length; ) {
      final double high = highs[i];
      final double low = lows[i];
      final double close = closes[i];

      // multi-vote
      final int vote = vote(ph, high) + vote(pl, low) + vote(pc, close);
      output[i] = pmvobv += (volumes[i] * vote);

      ph = high;
      pl = low;
      pc = close;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final int vote(final double previous, final double current) {
    return (current > previous) ?  ONE :
           (current < previous) ? -ONE :
                                   ZERO;
  }

}
