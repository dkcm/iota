/**
 * VHF.java  v0.2  22 January 2015 7:48:04 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Vertical Horizontal Filter (VHF) by Adam White
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V18/C07/063VERT.pdf<br>
 * http://www.incrediblecharts.com/indicators/vertical_horizontal_filter.php<br>
 * http://www.ta-guru.com/Book/TechnicalAnalysis/TechnicalIndicators/VerticalHorizontalFilter.php5<br>
 * http://etfhq.com/blog/2011/02/09/vertical-horizontal-filter/<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VHF extends AbstractIndicator {

  private final MinimumPrice minPrice;
  private final MaximumPrice maxPrice;

  public VHF() {
    this(TWENTY_EIGHT);
  }

  public VHF(final int period) {
    super(period, period);

    minPrice = new MinimumPrice(period);
    maxPrice = new MaximumPrice(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Select the number of periods (n) to include in the indicator.
    // 2. Determine the highest closing price (HCP) in n periods.
    // 3. Determine the lowest closing price (LCP) in n periods.
    // 4. Calculate the range of closing prices in n periods:
    //             HCP - LCP
    // 5. Next, calculate the movement in closing price for each period:
    //             Closing price [today] - Closing price [yesterday]
    // 6. Add up all price movements for n periods, disregarding whether they
    //    are up or down:
    //             Sum of absolute values of ( Close [today] - Close [yesterday] ) for n periods
    // 7. Divide Step 4 by Step 6:
    //             VHF = (HCP - LCP) / (Sum of absolute values for n periods)

    final int size = values.length;

    // compute highs and lows
    final TimeSeries closes = new TimeSeries(EMPTY, size);
    for (int i = ZERO; i < size; ++i) {
      closes.value(values[i], i);
    }
    final TimeSeries min = minPrice.generate(closes).get(ZERO);
    final TimeSeries max = maxPrice.generate(closes).get(ZERO);

    // compute range of closing prices
    final double[] range = computeRange(min, max);

    // compute absolute movement in closing price
    final double[] cpm = computeAbsoluteMovement(values);

    // compute sum
    final double[] sum = sum(period, cpm);

    // compute indicator
    for (int i = ZERO, j = ONE; i < output.length; ++i, ++j) {
      output[i] = range[j] / sum[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double[] computeRange(final TimeSeries min, final TimeSeries max) {
    final double[] range = new double[max.size()];
    for (int i = ZERO; i < range.length; ++i) {
      final double hcp = max.value(i); // highest closing price
      final double lcp = min.value(i); // lowest closing price
      range[i] = hcp - lcp;
    }
    return range;
  }

  private static final double[] computeAbsoluteMovement(final double[] prices) {
    int day = ZERO;
    double yesterday = prices[day];
    final double[] movements = new double[prices.length - ONE];
    for (int i = ZERO; i < movements.length; ++i) {
      final double today = prices[++day];
      movements[i] = Math.abs(today - yesterday);
      yesterday = today;
    }
    return movements;
  }

}
