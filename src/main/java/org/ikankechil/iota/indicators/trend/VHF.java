/**
 * VHF.java  v0.1 22 January 2015 7:48:04 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Vertical Horizontal Filter (VHF)
 * <p>
 * http://www.incrediblecharts.com/indicators/vertical_horizontal_filter.php
 * http://www.ta-guru.com/Book/TechnicalAnalysis/TechnicalIndicators/VerticalHorizontalFilter.php5
 * http://etfhq.com/blog/2011/02/09/vertical-horizontal-filter/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VHF extends AbstractIndicator {

  private final int maxLookback;

  public VHF() {
    this(TWENTY_EIGHT);
  }

  public VHF(final int period) {
    super(period, TA_LIB.sumLookback(period) + ONE);

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
    // 1. Select the number of periods (n) to include in the indicator.
    // 2. Determine the highest closing price (HCP) in n periods.
    // 3. Determine the lowest closing price (LCP) in n periods.
    // 4. Calculate the range of closing prices in n periods:
    //             HCP - LCP
    // 5. Next, calculate the movement in closing price for each period:
    //             Closing price [today] - Closing price [yesterday]
    // 6. Add up all price movements for n periods, disregarding whether they
    //    are up or down:
    //             Sum of absolute values of ( Close [today] - Close [yesterday] ) for n periods
    // 7. Divide Step 4 by Step 6:
    //             VHF = (HCP - LCP) / (Sum of absolute values for n periods)

    final int size = values.length;

    // compute highest closing price
    final double[] hcp = new double[size - maxLookback];
    RetCode outcome = TA_LIB.max(start,
                                 end,
                                 values,
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 hcp);
    throwExceptionIfBad(outcome, null);

    // compute lowest closing price
    final double[] lcp = new double[hcp.length];
    outcome = TA_LIB.min(start,
                         end,
                         values,
                         period,
                         outBegIdx,
                         outNBElement,
                         lcp);
    throwExceptionIfBad(outcome, null);

    // compute range of closing prices
    final double[] range = new double[lcp.length];
    for (int i = ZERO; i < range.length; ++i) {
      range[i] = hcp[i] - lcp[i];
    }

    // compute absolute movement in closing price
    double cpYesterday = values[ZERO];
    final double[] cpm = new double[size - ONE];
    for (int i = ZERO, today = ONE; i < cpm.length; ++i, ++today) {
      final double cpToday = values[today];
      cpm[i] = Math.abs(cpToday - cpYesterday);
      cpYesterday = cpToday;
    }

    // compute sum
    final double[] sum = new double[cpm.length - maxLookback];
    outcome = TA_LIB.sum(ZERO,
                         cpm.length - ONE,
                         cpm,
                         period,
                         outBegIdx,
                         outNBElement,
                         sum);

    // compute indicator
    for (int i = 0, j = ONE; i < output.length; ++i, ++j) {
      output[i] = range[j] / sum[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
