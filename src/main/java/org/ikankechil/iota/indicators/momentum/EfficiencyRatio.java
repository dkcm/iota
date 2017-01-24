/**
 * EfficiencyRatio.java  v0.1  22 January 2017 11:11:23 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Efficiency Ratio (ER) by Perry Kaufman
 *
 * <p>http://etfhq.com/blog/2011/02/07/kaufmans-efficiency-ratio/<br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:kaufman_s_adaptive_moving_average#efficiency_ratio_er<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class EfficiencyRatio extends AbstractIndicator {

  public EfficiencyRatio() {
    this(TEN);
  }

  public EfficiencyRatio(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // ER = Direction / Volatility
    // Where:
    //  Direction = ABS (Close – Close[n])
    //  Volatility = n Sum (ABS(Close – Close[1]))
    //  n = The efficiency ratio period.

    final double[] absPriceMovements = new double[values.length - ONE];

    // compute volatility
    int v = ZERO;
    double volatility = ZERO;
    while (v < period) {
      volatility += absPriceMovements[v] = absPriceMovement(values, v, ++v);
    }

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double direction = absPriceMovement(values, i, v);

      output[i] = direction / volatility;

      // add last remove first
      volatility += absPriceMovements[v] = absPriceMovement(values, v, ++v);
      volatility -= absPriceMovements[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double absPriceMovement(final double[] values,
                                               final int initialIndex,
                                               final int finalIndex) {
    return Math.abs(values[finalIndex] - values[initialIndex]);
  }

}
