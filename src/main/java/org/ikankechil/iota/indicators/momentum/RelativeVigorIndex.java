/**
 * RelativeVigorIndex.java  v0.2 7 July 2015 8:00:43 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.HashMap;
import java.util.Map;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Relative Vigor Index by John Ehlers
 * <p>
 * http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RelativeVigorIndex extends AbstractIndicator {

  private static final Map<Integer, double[]> COEFFICIENTS = new HashMap<>();
  private static final double[]               BASIC        = new double[] { ONE, TWO, TWO, ONE };

  static { // initialise coefficients
    COEFFICIENTS.put(ONE, BASIC);
    // default period
    COEFFICIENTS.put(TEN, new double[] { 1, 3, 5, 6, 6, 6, 6, 6, 6, 6, 5, 3, 1 });
  }

  public RelativeVigorIndex() {
    this(TEN);
  }

  public RelativeVigorIndex(final int period) {
    super(period, period + TWO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Value1 = ((Close - Open) + 2*(Close[1] - Open[1]) + 2*(Close[2] - Open[2]) + (Close[3] - Open[3]))/6;
    // Value2 = ((High - Low) + 2*(High[1] - Low[1]) + 2*(High[2] - Low[2]) + (High[3] - Low[3]))/6;
    // Num = 0;
    // Denom = 0;
    // For count = 0 to Length -1 begin
    // Num = Num + Value1[count];
    // Denom = Denom + Value2[count];
    // If Denom <> 0 then RVI = Num / Denom;

    final double[] opens = ohlcv.opens();
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    // compute indicator
    final double[] coefficients = coefficients(period);
    for (int i = ZERO; i < output.length; ++i) {
      double so = ZERO;
      double sh = ZERO;
      double sl = ZERO;
      double sc = ZERO;
      for (int c = ZERO, j = i; c < coefficients.length; ++c, ++j) {
        final double coefficient = coefficients[c];
        so += opens[j] * coefficient;
        sh += highs[j] * coefficient;
        sl += lows[j] * coefficient;
        sc += closes[j] * coefficient;
      }

      // compute vigour and range
      final double sumVigour = sc - so;
      final double sumRange = sh - sl;
      if (sumRange != ZERO) {
        // division by weight omitted as it ultimately cancels out
        output[i] = sumVigour / sumRange;
      }

//      // classic algorithm v0.1
//      double sumVigour = ZERO;
//      double sumRange = ZERO;
//      for (int j = ZERO, i0 = i, i1 = i0 + ONE, i2 = i1 + ONE, i3 = i2 + ONE;
//           j < period;
//           ++j, ++i0, ++i1, ++i2, ++i3) {
//        // compute vigour
//        final double vigour0 = closes[i0] - opens[i0];
//        final double vigour1 = (closes[i1] - opens[i1]) * TWO;
//        final double vigour2 = (closes[i2] - opens[i2]) * TWO;
//        final double vigour3 = closes[i3] - opens[i3];
//        final double vigour = (vigour0 + vigour1 + vigour2 + vigour3);
//
//        // compute range
//        final double range0 = highs[i0] - lows[i0];
//        final double range1 = (highs[i1] - lows[i1]) * TWO;
//        final double range2 = (highs[i2] - lows[i2]) * TWO;
//        final double range3 = highs[i3] - lows[i3];
//        final double range = (range0 + range1 + range2 + range3);
//
//        sumVigour += vigour;
//        sumRange += range;
//      }
//
//      // division by weight (i.e. 6) omitted as it ultimately cancels out
//      if (sumRange != ZERO) {
//        output[i] = sumVigour / sumRange;
//      }
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double[] coefficients(final int period) {
    double[] coefficients = COEFFICIENTS.get(period);
    if (coefficients == null) {
      COEFFICIENTS.put(period, coefficients = new double[period + THREE]);
      for (int i = ZERO; i < period; ++i) {
        for (int j = ZERO, c = i; j < BASIC.length; ++j, ++c) {
          coefficients[c] += BASIC[j];
        }
      }
      logger.debug("New RelativeVigorIndex coefficients (period: {}: {})",
                   period,
                   coefficients);
    }
    return coefficients;
  }

}
