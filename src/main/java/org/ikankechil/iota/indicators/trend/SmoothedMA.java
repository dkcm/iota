/**
 * SmoothedMA.java  v0.1  27 April 2019 12:29:29 AM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Smoothed Moving Average
 *
 * <p>References:
 * <li>https://www.metatrader5.com/en/terminal/help/indicators/trend_indicators/ma#smma
 * <li>http://www2.wealth-lab.com/WL5Wiki/SMMA.ashx
 * <li>http://www.fxcorporate.com/help/MS/NOTFIFO/i_SMMA.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SmoothedMA extends AbstractIndicator implements MA {

  private final double inversePeriod;

  public SmoothedMA(final int period) {
    super(period, period - ONE);

    inversePeriod = ONE / (double) period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // SmoothedMA(i) = (SmoothedMA(i - 1) * (N - 1) + Close(i)) / N

    // first value is SMA
    int i = start;
    int v = i + period;
    double previous = output[i] = sum(values, i, v) * inversePeriod;

    // subsequent values
    while (++i < output.length) {
      output[i] = previous = smoothed(previous, lookback, values[v++], inversePeriod);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  protected static double smoothed(final double previous,
                                   final int lookback,
                                   final double price,
                                   final double inversePeriod) {
    return (previous * lookback + price) * inversePeriod;
  }

}
