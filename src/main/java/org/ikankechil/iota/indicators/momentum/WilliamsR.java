/**
 * WilliamsR.java  v0.2  5 December 2014 4:01:07 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Williams %R by Larry Williams
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:williams_r<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class WilliamsR extends AbstractIndicator {

  public WilliamsR() {
    this(FOURTEEN);
  }

  public WilliamsR(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // %R = (Highest High - Close)/(Highest High - Lowest Low) * -100
    // Lowest Low = lowest low for the look-back period
    // Highest High = highest high for the look-back period
    // %R is multiplied by -100 correct the inversion and move the decimal.

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    // compute indicator
    int from = ZERO;
    int to = lookback;

    int maxI = from;
    double max = highs[maxI];
    for (int i = maxI + ONE; i <= to; ++i) {
      final double d = highs[i];
      if (d > max) {
        max = d;
        maxI = i;
      }
    }

    int minI = from;
    double min = lows[minI];
    for (int i = minI + ONE; i <= to; ++i) {
      final double d = lows[i];
      if (d < min) {
        min = d;
        minI = i;
      }
    }

    int c = lookback;
    double close = closes[c];
    output[from] = r(max, min, close);

    for (++to; ++from < output.length; ++to) {
      if (maxI < from) {
        maxI = from;
        max = highs[maxI];
        for (int i = maxI + ONE; i <= to; ++i) {
          final double d = highs[i];
          if (d > max) {
            max = d;
            maxI = i;
          }
        }
      }
      else if (max < highs[to]) {
        max = highs[to];
        maxI = to;
      }

      if (minI < from) {
        minI = from;
        min = lows[minI];
        for (int i = minI + ONE; i <= to; ++i) {
          final double d = lows[i];
          if (d < min) {
            min = d;
            minI = i;
          }
        }
      }
      else if (min > lows[to]) {
        min = lows[to];
        minI = to;
      }

      close = closes[++c];
      output[from] = r(max, min, close);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success; // 1203ms 1187ms 1232ms

//    return TA_LIB.willR(start,
//                        end,
//                        highs,
//                        lows,
//                        closes,
//                        period,
//                        outBegIdx,
//                        outNBElement,
//                        output); // 1373ms 1376ms
  }

  private static final double r(final double max, final double min, final double close) {
    return (max - close) / (max - min) * -HUNDRED_PERCENT;
  }

}
