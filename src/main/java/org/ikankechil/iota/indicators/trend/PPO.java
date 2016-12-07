/**
 * PPO.java  v0.2  27 November 2014 5:54:58 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Percentage Price Oscillator (PPO)
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:price_oscillators_ppo
 * http://www.investopedia.com/terms/p/ppo.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PPO extends IndicatorWithSignalLine {

  private final int fast;
  private final int slow;

  public PPO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public PPO(final int fast, final int slow, final int signal) {
    super(signal, Math.max(fast, slow) + signal - TWO);
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Percentage Price Oscillator (PPO): ((12-day EMA - 26-day EMA) / 26-day EMA) x 100
    // Signal Line: 9-day EMA of PPO
    // PPO Histogram: PPO - Signal Line

    // compute fast and slow EMAs
    final double[] fastEMAs = ema(values, fast);
    final double[] slowEMAs = ema(values, slow);

    // compute indicator
    for (int i = ZERO, j = slow - fast; i < output.length; ++i, ++j) {
      output[i] = HUNDRED_PERCENT * (fastEMAs[j] / slowEMAs[i] - ONE);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
