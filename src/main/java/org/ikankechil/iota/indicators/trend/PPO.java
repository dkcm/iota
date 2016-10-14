/**
 * PPO.java  v0.1  27 November 2014 5:54:58 pm
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.IndicatorWithSignalLine;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Percentage Price Oscillator (PPO)
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:price_oscillators_ppo
 * <a href="http://www.investopedia.com/terms/p/ppo.asp">http://www.investopedia.com/terms/p/ppo.asp</a><br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PPO extends IndicatorWithSignalLine {

  private final int fast;
  private final int slow;
//  private final EMA slowEMA;
//  private final EMA fastEMA;

  public PPO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public PPO(final int fast, final int slow, final int signal) {
    super(signal, TA_LIB.ppoLookback(fast, slow, MAType.Ema) + TA_LIB.emaLookback(signal));
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;

//    fastEMA = new EMA(fast);
//    slowEMA = new EMA(slow);
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

//    // alternatives
//    final double[] fastEMAs = ema(values, fast);
//    final double[] slowEMAs = ema(values, slow);
//
//    final double[] fastEMAs = new double[values.length - fast + ONE];
//    RetCode outcome = fastEMA.compute(start,
//                                      end,
//                                      values,
//                                      outBegIdx,
//                                      outNBElement,
//                                      fastEMAs);
//    throwExceptionIfBad(outcome, null);
//
//    final double[] slowEMAs = new double[output.length];
//    outcome = slowEMA.compute(start,
//                              end,
//                              values,
//                              outBegIdx,
//                              outNBElement,
//                              slowEMAs);
//    throwExceptionIfBad(outcome, null);
//
//    // compute indicator
//    for (int i = ZERO, j = slow - fast; i < output.length; ++i, ++j) {
//      output[i] = HUNDRED_PERCENT * (fastEMAs[j] / slowEMAs[i] - ONE);
//    }
//
//    outBegIdx.value = lookback;
//    outNBElement.value = output.length;
//    return RetCode.Success;

    return TA_LIB.ppo(start,
                      end,
                      values,
                      fast,
                      slow,
                      MAType.Ema,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
