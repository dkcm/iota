/**
 * EMA.java  v0.4  8 December 2014 7:06:59 PM
 *
 * Copyright © 2014-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Exponential Moving Average (EMA)
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_averages<br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class EMA extends AbstractIndicator implements MA {

  private final double alpha;

  public EMA(final int period) {
    this(period, TWO / (double) (period + ONE));
  }

  public EMA(final int period, final double alpha) {
    super(period, period - ONE);

    this.alpha = alpha;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // EMA = alpha * Price + (1 – alpha) * EMA[1]
    //     = EMA[1] + alpha * (Price – EMA[1])
    // where
    // EMA[1] = value of the EMA one bar ago.
    // alpha = 2 / (period + 1)

    // first value is SMA
    int v = ZERO;
    double previous = values[v];
    while (++v < period) {
      previous += values[v];
    }
    int i = ZERO;
    output[i] = previous /= period;

    // subsequent values are EMA
    while (++i < output.length) {
//      output[i] = previous += alpha * (values[v++] - previous);
      output[i] = previous = ema(alpha, values[v++], previous);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  /**
   * Computes the current EMA value.
   *
   * @param alpha smoothing factor
   * @param price current price
   * @param previousEMA previous EMA value
   * @return current EMA value
   */
  public static final double ema(final double alpha, final double price, final double previousEMA) {
    return previousEMA + alpha * (price - previousEMA);
  }

}
