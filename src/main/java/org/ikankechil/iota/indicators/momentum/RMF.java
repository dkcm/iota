/**
 * RMO.java  v0.1  23 April 2018 11:11:59 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static java.lang.Math.*;
import static org.ikankechil.iota.indicators.momentum.RMF.Medians.*;

import java.util.Arrays;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.EMA;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Recursive Median Filter (RMF) by John Ehlers
 *
 * <p>References:
 * <li>http://traders.com/Documentation/FEEDbk_docs/2018/03/TradersTips.html<br>
 * <li>http://fxcodebase.com/code/viewtopic.php?f=17&t=65722<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMF extends AbstractIndicator {

  private final int median;
  private final EMA ema;

  public RMF() {
    this(TWELVE);
  }

  public RMF(final int period) {
    this(period, FIVE);
  }

  public RMF(final int period, final int median) {
    super(period, (period + median - TWO));
    throwExceptionIfNegative(median);

    this.median = median;
    ema = new EMA(period, computeAlpha(ONE, period));
  }

  static double computeAlpha(final double modifier, final int period) {
    final double angle = modifier * Math.PI * TWO / period;
    final double cos = cos(angle);
    return (cos + sin(angle) - ONE) / cos;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Recursive Median = EMA of 5-bar median filter

    final double[] medians = computeMedian(median, values);

    // compute indicator
    final double[] emas = ema.generate(new TimeSeries(EMPTY, new String[medians.length], medians)).get(ZERO).values();
    System.arraycopy(emas, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static double[] computeMedian(final int median, final double... values) {
    final double[] medians = new double[values.length - (median - ONE)];
    final double[] section = new double[median];
    final Medians mid = (median % TWO > ZERO) ? ODD : EVEN;
    for (int i = ZERO; i < medians.length; ++i) {
      System.arraycopy(values, i, section, ZERO, median);
      Arrays.sort(section);
      medians[i] = mid.compute(section);
    }
    return medians;
  }

  enum Medians {
    EVEN {
      @Override
      double compute(final double... values) {
        final int mid = values.length >> ONE;
        return (values[mid - ONE] + values[mid]) * HALF;
      }
    },
    ODD {
      @Override
      double compute(final double... values) {
        return values[values.length >> ONE];
      }
    };

    abstract double compute(double... values);

  }

}
