/**
 * ZScore.java  v0.1  16 April 2018 10:45:44 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
/**
 * Z-Score
 *
 * <p>References:
 * <li>http://traders.com/documentation/feedbk_docs/2003/02/TradersTips/TradersTips.html<br>
 * <li>https://www.quantshare.com/item-382-z-score-standard-score<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZScore extends AbstractIndicator {

  private final StandardDeviation stdDev;

  public ZScore(final int period) {
    super(period, (period - ONE));

    stdDev = new StandardDeviation(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // z = (x - mean) / (standard deviation)

    // compute mean
    final double[] mean = sma(values, period);

    // compute standard deviation
    stdDev.compute(start, end, values, outBegIdx, outNBElement, output);

    // compute indicator
    for (int i = ZERO, v = lookback; i < output.length; ++i, ++v) {
      output[i] = (values[v] - mean[i]) / output[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
