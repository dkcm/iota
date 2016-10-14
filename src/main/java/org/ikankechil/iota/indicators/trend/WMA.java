/**
 * WMA.java  v0.2  8 December 2014 8:37:37 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Weighted Moving Average (WMA)
 *
 * <p>http://www.investopedia.com/articles/technical/060401.asp<br>
 * http://www.investopedia.com/ask/answers/071414/whats-difference-between-moving-average-and-weighted-moving-average.asp<br>
 * https://www.tradestation.com/education/labs/analysis-concepts/a-comparative-study-of-moving-averages<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class WMA extends AbstractIndicator {

  private final double baseWeight;
  private final double maxWeight;

  public WMA(final int period) {
    super(period, period - ONE);

    baseWeight = (double) TWO / (period * (period + ONE));
    maxWeight = period * baseWeight;
    // use trapezium area formula: (h / 2) * (a + b)
    // 1, 2: 3
    // 1, 2, 3: 6
    // 1, 2, 3, 4: 10
    // 1, 2, 3, 4, 5: 15
    // 1, 2, 3, 4, 5, 6: 21
    // 1, 2, 3, 4, 5, 6, 7: 28
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // compute first WMA
    int i = ZERO;
    double wma = output[i] = wma(values, i);

    // compute offsets before weighting
    final double[] offsets = sum(period, values); // TODO distribute offset computation

    // compute indicator
    for (int v = period; v < values.length; ++v) {
      final double sum = offsets[i] * baseWeight;
      output[++i] = wma += (values[v] * maxWeight) - sum;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;

//    return TA_LIB.wma(start,
//                      end,
//                      values,
//                      period,
//                      outBegIdx,
//                      outNBElement,
//                      output);
  }

  private final double wma(final double[] values, final int start) {
    double weighting = baseWeight;
    int i = start;
    double sum = values[i] * weighting;
    for (int p = ONE; p < period; ++p) {
      weighting += baseWeight;
      sum += values[++i] * weighting;
    }
    return sum;
  }

}
