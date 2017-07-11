/**
 * SWMA.java  v0.1  10 January 2017 11:13:48 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Sine Weighted Moving Average (SWMA) by Patrick Lafferty
 *
 * <p>http://etfhq.com/blog/2010/10/19/sine-weighted-moving-average/<br>
 * http://forum.esignal.com/forum/efs-development/efs-library-discussion-board/stocks-commodities-magazine/30423-1999-jun-sine-weighted-moving-average-by-patrick-lafferty<br>
 * https://user42.tuxfamily.org/chart/manual/Sine-Weighted-Moving-Average.html<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SWMA extends AbstractIndicator implements MA {

  private static final Map<Integer, double[]> SINE_WEIGHTS = new ConcurrentHashMap<>();

  public SWMA() {
    this(THIRTY);
  }

  public SWMA(final int period) {
    super(period, period - ONE);

    // compute weights
    if (!SINE_WEIGHTS.containsKey(period)) {
      double sum = ZERO;
      final double baseAngle = Math.PI / (period + ONE);
      double angle = baseAngle;
      final double[] sineWeights = new double[period + ONE];
      for (int i = ONE; i < sineWeights.length; ++i, angle += baseAngle) {
        sum += sineWeights[i] = Math.sin(angle);
      }
      sineWeights[ZERO] = ONE / sum; // inverse sum of sine weights in first element

      SINE_WEIGHTS.put(period, sineWeights);
    }
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // To calculate the Sine Weighted Moving Average multiply the sine value for
    // each period by the close price for that period, add it all up and divide
    // the result by the sum of the sine weights.

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = swma(i, values);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final double swma(final int start, final double[] values) {
    double sum = ZERO;
    final double[] sineWeights = SINE_WEIGHTS.get(period);
    for (int i = ONE, v = start; i < sineWeights.length; ++i, ++v) {
      sum += sineWeights[i] * values[v];
    }
    return sum * sineWeights[ZERO];
  }

}
