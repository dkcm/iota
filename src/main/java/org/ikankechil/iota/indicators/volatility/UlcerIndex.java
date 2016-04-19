/**
 * UlcerIndex.java	v0.1	1 August 2015 9:06:58 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ulcer Index by Peter G. Martin and Byron McCann
 * <p>
 * "Ulcer Index measures the depth and duration of percentage drawdowns in price
 * from earlier highs. The greater a drawdown in value, and the longer it takes
 * to recover to earlier highs, the higher the UI. Technically, it is the square
 * root of the mean of the squared percentage drawdowns in value. The squaring
 * effect penalizes large drawdowns proportionately more than small drawdowns."
 * - Peter G. Martin
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:
 * ulcer_index
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class UlcerIndex extends AbstractIndicator {

  private final int maxLookback;

  public UlcerIndex() {
    this(FOURTEEN);
  }

  public UlcerIndex(final int period) {
    super(period, TA_LIB.maxLookback(period) + TA_LIB.smaLookback(period));

    maxLookback = TA_LIB.maxLookback(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Percent-Drawdown = ((Close - 14-period Max Close)/14-period Max Close) x 100
    // Squared Average = (14-period Sum of Percent-Drawdown Squared)/14
    // Ulcer Index = Square Root of Squared Average

    // compute max
    final double[] max = new double[values.length - maxLookback];
    final RetCode outcome = TA_LIB.max(start,
                                       end,
                                       values,
                                       period,
                                       outBegIdx,
                                       outNBElement,
                                       max);
    throwExceptionIfBad(outcome, null);

    // compute Percent-Drawdown Squared
    final double[] percentDrawdownSquared = new double[max.length];
    for (int p = ZERO, i = maxLookback; p < percentDrawdownSquared.length; ++p, ++i) {
      final double percentDrawdown = (values[i] / max[p] - ONE) * HUNDRED_PERCENT;
//      final double percentDrawdown = (values[i] / max[p] - ONE);
      percentDrawdownSquared[p] = percentDrawdown * percentDrawdown;
    }

    // compute Squared Average
    final double[] squaredAverage = sma(percentDrawdownSquared, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = Math.sqrt(squaredAverage[i]);
//      output[i] = Math.sqrt(squaredAverage[i]) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
