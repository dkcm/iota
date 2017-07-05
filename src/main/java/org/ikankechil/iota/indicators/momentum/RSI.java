/**
 * RSI.java  v0.5  4 December 2014 12:17:42 PM
 *
 * Copyright © 2014-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Relative Strength Index (RSI) by J. Welles Wilder
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?st=rsi+indicator&id=chart_school:technical_indicators:relative_strength_index_rsi<br>
 * <li>http://www.investopedia.com/terms/r/rsi.asp<br>
 * <li>https://en.wikipedia.org/wiki/Relative_strength_index
 *
 *
 * @author Daniel Kuan
 * @version 0.5
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

    // compute sum gain and loss (first value)
    double sumGain = ZERO;
    double sumLoss = ZERO;
    int v = ZERO;
    double previous = values[v];
    for (; ++v <= period; ) {
      final double current = values[v];
      final double change = current - previous;
      if (change >= ZERO) {
        sumGain += change;
      }
      else {
        sumLoss -= change;
      }
      previous = current;
    }

    // compute indicator (first value)
    int i = ZERO;
    double rsi = computeRSI(sumGain, sumLoss);
    output[i] = rsi;

    // compute sum gain and loss (subsequent values)
    for (; v < values.length; ++v) {
      final double current = values[v];
      final double change = current - previous;

      if (change >= ZERO) { // gain
        sumGain = sumGain * smoothingFactor + change;
        sumLoss *= smoothingFactor;
      }
      else {                // loss
        sumGain *= smoothingFactor;
        sumLoss = sumLoss * smoothingFactor - change;
      }

      // alternative
//      sumGain *= smoothingFactor;
//      sumLoss *= smoothingFactor;
//      if (change >= ZERO) { // gain
//        sumGain += change;
//      }
//      else {                // loss
//        sumLoss -= change;
//      }

      previous = current;

      // compute indicator (subsequent values)
      output[++i] = rsi = computeRSI(sumGain, sumLoss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected double computeRSI(final double sumGain, final double sumLoss) {
    return rsi(sumGain, sumLoss);
  }

  protected static final double rsi(final double averageGain, final double averageLoss) {
    // RSI = 100 - (100 / (ONE + RS))
    //     = 100 * average gain / (average gain + average loss)
    // where
    // RS = average gain / average loss;
    return HUNDRED_PERCENT * averageGain / (averageGain + averageLoss);
  }

  protected static final double rsi(final int period, final double[] values, final int from) {
    double sumGain = ZERO;
    double sumLoss = ZERO;
    int v = from;
    double previous = values[v];
    final int to = from + period + ONE;
    for (; ++v < to; ) {
      final double current = values[v];
      final double change = current - previous;
      if (change >= ZERO) {
        sumGain += change;
      }
      else {
        sumLoss -= change;
      }
      previous = current;
    }

    return rsi(sumGain, sumLoss);
  }

}
