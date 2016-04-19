/**
 * REI.java v0.1 19 January 2015 7:03:45 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Range Expansion Index (REI) by Thomas DeMark
 * <p>
 * http://en.wikipedia.org/wiki/Range_expansion_index
 * http://user42.tuxfamily.org/chart/manual/TD-Range-Expansion-Index.html#TD-Range-Expansion-Index
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class REI extends AbstractIndicator {

  private static final String N_M_AND_S_GENERATED = "n, m and s generated for {}";

  public REI() {
    this(EIGHT);
  }

  public REI(final int period) {
    super(period, period + (TWO + SIX));

    // Computation:
    // The calculation is somewhat similar to an RSI (see Relative Strength
    // Index) but looks at 2-day changes in the daily high and daily low values
    // and smooths with a 5-day SMA (see Simple Moving Average). Changes are
    // ignored if the current day in not either within or covering price action
    // from 5 or 6 days ago. That test effectively holds the indicator around
    // zero while prices are making breakaway runs.

    // Strategy:
    // DeMark regarded values above +45 or below -45 as overbought or oversold.
    // Such a reading maintained for up to five days suggests a reversal, except
    // that if it remains there for 6 or more days then the signal may be
    // unreliable and trading should be avoided.
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // S1 = Sum_k(n_j * m_j * s_j)
    //
    //   if ((High[j – 2] < Close[j - 7]) && (High[j - 2] < Close[j - 8]) &&
    //       (High[j] < High[j - 5]) && (High[j] < High[j - 6]))
    //   then n_j = 0
    //   else n_j = 1
    //
    //   if ((Low[j – 2] > Close[j – 7]) && (Low[j – 2] > Close[j – 8]) &&
    //       (Low[j] > Low[j – 5]) && (Low[j] > Low[j – 6]))
    //   then m_j = 0
    //   else m_j = 1
    //
    //   s_j = High[j] - High[j - 2] + Low[j] - Low[j - 2]
    //
    // S2 = Sum_k( |High[j] - High[j - 2]| + |Low[j] - Low[j - 2]| )
    //
    // REI = S1 / S2 * 100

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    final int size = ohlcv.size();

    // compute n, m, s
    final int[] n = new int[size - EIGHT];
    final int[] m = new int[n.length];
//    final boolean[] n = new boolean[size - EIGHT]; // TODO optimise
//    final boolean[] m = new boolean[n.length];
    final double[] s = new double[m.length];
    final double[] sh = new double[s.length];
    final double[] sl = new double[sh.length];

    for (int today = EIGHT, j = ZERO, t7 = today - SEVEN, t8 = today - EIGHT, t2 = today - TWO, t5 = today - FIVE, t6 = today - SIX;
         today < size;
         ++today, ++j, ++t7, ++t8, ++t2, ++t5, ++t6) {
      final double c7 = closes[t7];
      final double c8 = closes[t8];

      final double h = highs[today];
      final double h2 = highs[t2];
      final double h5 = highs[t5];
      final double h6 = highs[t6];

      n[j] = (h2 < c7 && h2 < c8 && h < h5 && h < h6) ? ZERO : ONE;
//      n[j] = !(h2 < c7 && h2 < c8 && h < h5 && h < h6);

      final double l = lows[today];
      final double l2 = lows[t2];
      final double l5 = lows[t5];
      final double l6 = lows[t6];

      m[j] = (l2 > c7 && l2 > c8 && l > l5 && l > l6) ? ZERO : ONE;
//      m[j] = !(l2 > c7 && l2 > c8 && l > l5 && l > l6);

      sh[j] = h - h2;
      sl[j] = l - l2;
      s[j] = sh[j] + sl[j];
    }
    logger.debug(N_M_AND_S_GENERATED, name);

    for (int i = ZERO; i < output.length; ++i) {
      double s1 = ZERO;
      double s2 = ZERO;
      // compute S1 and S2
      for (int j = ZERO, k = i + j; j < period; ++j, ++k) {
        s1 += n[k] * m[k] * s[k];
//        s1 += (n[k] && m[k]) ? s[k] : ZERO;
        s2 += Math.abs(sh[k]) + Math.abs(sl[k]);
      }

      // compute indicator
      output[i] = (s1 / s2) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
