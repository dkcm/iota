/**
 * MedianFilter.java  v0.1  14 May 2018 8:33:38 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.momentum.MedianFilter.Medians.*;

import java.util.Arrays;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Median Filter by John Ehlers
 *
 * <p>References:
 * <li>https://www.mesasoftware.com/papers/EhlersFilters.pdf<br>
 * <li>http://www.stockspotter.com/Files/whatsthedifference.pdf<br>
 * <li>http://traders.com/Documentation/FEEDbk_docs/2018/03/TradersTips.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MedianFilter extends AbstractIndicator {

  public MedianFilter() {
    this(FIVE);
  }

  public MedianFilter(final int period) {
    super(period, (period - ONE));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // compute indicator
    final double[] medians = medianFilter(period, values);
    System.arraycopy(medians, ZERO, output, ZERO, medians.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  public static double[] medianFilter(final int period, final double... values) {
    final double[] medians = new double[values.length - (period - ONE)];
    final double[] section = new double[period];
    final Medians median = toMedian(period);
    for (int i = ZERO; i < medians.length; ++i) {
      System.arraycopy(values, i, section, ZERO, period);
      Arrays.sort(section);
      medians[i] = median.compute(section);
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

    static Medians toMedian(final int period) {
      return (period % TWO > ZERO) ? ODD : EVEN;
    }

  }


}
