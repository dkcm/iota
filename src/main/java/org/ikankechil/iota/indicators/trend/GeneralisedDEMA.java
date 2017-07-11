/**
 * GeneralisedDEMA.java  v0.1  17 October 2016 7:38:40 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Generalised Double Exponential Moving Average by Tim Tillson
 *
 * <p>http://www.fmlabs.com/reference/default.htm?url=T3.htm<br>
 * http://www.forexfactory.com/attachment.php?attachmentid=709307&d=1306688278<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GeneralisedDEMA extends AbstractIndicator implements MA {

  private final double        volumeFactor;

  private static final double VOLUME_FACTOR = 0.7;

  public GeneralisedDEMA() {
    this(SEVEN);
  }

  public GeneralisedDEMA(final int period) {
    this(period, VOLUME_FACTOR);
  }

  public GeneralisedDEMA(final int period, final double volumeFactor) {
    super(period, (period << ONE) - TWO);
    throwExceptionIfNegative(volumeFactor);

    this.volumeFactor = volumeFactor;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // f: = (1 + a)x – ax^2
    // where a = volume factor, x = EMA
    //
    // GD(n,v) = EMA(n)*(1+v) - EMA(EMA(n))*v

    // compute EMAs
    final double[] ema1 = ema(values, period);
    final double[] ema2 = ema(ema1, period);

    // compute indicator
    for (int i = ZERO, j = ema1.length - ema2.length; i < output.length; ++i, ++j) {
//      output[i] = (ONE + volumeFactor) * ema1[j] - (volumeFactor * ema2[i]);
      final double e1 = ema1[j];
      output[i] = e1 + volumeFactor * (e1 - ema2[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
