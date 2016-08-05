/**
 * RSI.java  v0.3  4 December 2014 12:17:42 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Relative Strength Index (RSI) by J. Welles Wilder
 * <p>
 * http://stockcharts.com/school/doku.php?st=rsi+indicator&id=chart_school:technical_indicators:relative_strength_index_rsi
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class RSI extends AbstractIndicator {

  private final double smoothingFactor;

  public RSI() {
    this(FOURTEEN);
  }

  public RSI(final int period) {
    super(period, period);

    smoothingFactor = ONE - (ONE / (double) period); // = (period - 1) / period
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // RSI = 100 - (100 / (1 + RS))
    // RS = Average Gain / Average Loss
    //
    // The very first calculations for average gain and average loss are simple 14 period averages.
    // First Average Gain = Sum of Gains over the past 14 periods / 14.
    // First Average Loss = Sum of Losses over the past 14 periods / 14
    //
    // The second, and subsequent, calculations are based on the prior averages and the current gain loss:
    // Average Gain = [(previous Average Gain) x 13 + current Gain] / 14.
    // Average Loss = [(previous Average Loss) x 13 + current Loss] / 14.

    // compute average gain and loss (first value)
    double averageGain = ZERO;
    double averageLoss = ZERO;
    int v = ZERO;
    double previous = values[v];
    for (; ++v <= period; ) {
      final double current = values[v];
      final double change = current - previous;
      if (change >= ZERO) {
        averageGain += change;
      }
      else {
        averageLoss -= change;
      }
      previous = current;
    }

    // compute indicator (first value)
    int i = ZERO;
    double rsi = rsi(averageGain, averageLoss);
    output[i] = rsi;

    // compute average gain and loss (subsequent values)
    for (; v < values.length; ++v) {
      final double current = values[v];
      final double change = current - previous;

      if (change >= ZERO) { // gain
        averageGain = averageGain * smoothingFactor + change;
        averageLoss *= smoothingFactor;
      }
      else {                // loss
        averageGain *= smoothingFactor;
        averageLoss = averageLoss * smoothingFactor - change;
      }

//      averageGain *= smoothingFactor;
//      averageLoss *= smoothingFactor;
//      if (change >= ZERO) { // gain
//        averageGain += change;
//      }
//      else {                // loss
//        averageLoss -= change;
//      }

      previous = current;

      // compute indicator (subsequent values)
      output[++i] = rsi = rsi(averageGain, averageLoss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected static final double rsi(final double averageGain, final double averageLoss) {
    // RSI = 100 - (100 / (ONE + RS))
    //     = 100 * average gain / (average gain + average loss)
    // where
    // RS = average gain / average loss;
    return HUNDRED_PERCENT * averageGain / (averageGain + averageLoss);
  }

}
