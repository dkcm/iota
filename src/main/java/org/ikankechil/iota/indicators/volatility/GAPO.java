/**
 * GAPO.java v0.1 8 January 2015 1:12:12 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Gopalakrishnan Range Index (GAPO)
 * <p>
 * http://www.geniustrader.org/doc/GT/Indicators/GAPO.pm.html
 * http://user42.tuxfamily.org/chart/manual/Gopalakrishnan-Range-Index.html
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GAPO extends AbstractIndicator {

  private final double logP;

  public GAPO() {
    this(FIVE);
  }

  public GAPO(final int period) {
    super(period, TA_LIB.maxLookback(period));

    logP = Math.log(period);
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

    // compute highest highs
    final double[] highs = new double[ohlcv.size() - lookback];
    RetCode outcome = TA_LIB.max(start,
                                 end,
                                 ohlcv.highs(),
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 highs);
    throwExceptionIfBad(outcome, ohlcv);

    // compute lowest lows
    final double[] lows = new double[highs.length];
    outcome = TA_LIB.min(start,
                         end,
                         ohlcv.lows(),
                         period,
                         outBegIdx,
                         outNBElement,
                         lows);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = Math.log(highs[i] - lows[i]) / logP;
    }

    return RetCode.Success;
  }

}
