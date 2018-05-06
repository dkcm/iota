/**
 * ZScore.java  v0.2  16 April 2018 10:45:44 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.SMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Z-Score
 *
 * <p>References:
 * <li>http://traders.com/documentation/feedbk_docs/2003/02/TradersTips/TradersTips.html<br>
 * <li>https://www.quantshare.com/item-382-z-score-standard-score<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ZScore extends AbstractIndicator {

  private final SMA               sma;
  private final StandardDeviation stdDev;

  public ZScore(final int period) {
    this(period, ONE);
  }

  public ZScore(final int period, final double stdDev) {
    super(period, (period - ONE));

    sma = new SMA(period);
    this.stdDev = new StandardDeviation(period, stdDev);
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
    final double[] mean = sma.generate(new TimeSeries(EMPTY, new String[values.length], values)).get(ZERO).values();

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
