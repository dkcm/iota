/**
 * SwingIndex.java  v0.1  21 April 2019 11:30:37 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Swing Index by J. Welles Wilder
 *
 * <p>References:
 * <li>http://forex-indicators.net/accumulative-swing-index
 * <li>https://www.technicalindicators.net/indicators-technical-analysis/114-swing-index
 * <li>https://www.marketinout.com/technical_analysis.php?t=Swing_Index&id=100<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SwingIndex extends AbstractIndicator {

  private final double     limitMove;

  private static final int LIMIT_MOVE = 3;

  public SwingIndex() {
    this(LIMIT_MOVE);
  }

  /**
   *
   *
   * @param limitMove maximum allowable move in either direction from previous
   *          close
   */
  public SwingIndex(final double limitMove) {
    super(ONE);
    throwExceptionIfNegative(limitMove);

    this.limitMove = limitMove;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // SI = 50{[(C2 - C1) + 0.5(C2 - O2) + 0.25(C1 - O1)] / R}(K / L)
    // where:
    //   K = max(H2 - C1, L2 - C1)
    //   L = limit move in one direction
    //   R = { max
    //         H2 - C1: (H2 - C1) - 0.5(L2 - C1) + 0.25(C1 - O1)
    //         L2 - C1: (L2 - C1) - 0.5(H2 - C1) + 0.25(C1 - O1)
    //         H2 - L2: (H2 - L2) + 0.25(C1 - O1)
    //       }

    int yesterday = start;
    double pc = ohlcv.close(yesterday);
    double pb = pc - ohlcv.open(yesterday);

    for (int today = yesterday + ONE; yesterday < end; ++today) {
      final double high = ohlcv.high(today);
      final double low = ohlcv.low(today);

      final double h2c1 = Math.abs(high - pc);
      final double l2c1 = Math.abs(low - pc);
      final double k;         // max(h2c1, l2c1)

      final double qpb = QUARTER * pb;
      final double aqpb = Math.abs(qpb);
      final double h2l2 = Math.abs(high - low);
      double r = h2l2 + aqpb; // assume h2l2 largest
      if (h2c1 > l2c1) {
        k = h2c1;
        if (h2c1 > h2l2) {    // h2c1 largest
          r = h2c1 - (HALF * l2c1) + aqpb;
        }
      }
      else {
        k = l2c1;
        if (l2c1 > h2l2) {    // l2c1 largest
          r = l2c1 - (HALF * h2c1) + aqpb;
        }
      }

      final double close = ohlcv.close(today);
      final double body = close - ohlcv.open(today);
      final double n = close - pc + (HALF * body) + qpb;
      output[yesterday] = (FIFTY * n * k) / (r * limitMove);

      // shift forward
      yesterday = today;
      pc = close;
      pb = body;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
