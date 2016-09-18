/**
 * UlcerIndex.java  v0.2  1 August 2015 9:06:58 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ulcer Index by Peter G. Martin and Byron McCann
 * <p>
 * "Ulcer Index measures the depth and duration of percentage drawdowns in price
 * from earlier highs. The greater a drawdown in value, and the longer it takes
 * to recover to earlier highs, the higher the UI. Technically, it is the square
 * root of the mean of the squared percentage drawdowns in value. The squaring
 * effect penalizes large drawdowns proportionately more than small drawdowns."
 * - Peter G. Martin
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ulcer_index
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class UlcerIndex extends AbstractIndicator {

  private final MaximumPrice maxPrice;

  public UlcerIndex() {
    this(FOURTEEN);
  }

  public UlcerIndex(final int period) {
    super(period, (period << ONE) - TWO);

    maxPrice = new MaximumPrice(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Percent-Drawdown = ((Close - 14-period Max Close) / 14-period Max Close) x 100
    // Squared Average = (14-period Sum of Percent-Drawdown Squared) / 14
    // Ulcer Index = Square Root of Squared Average

    // compute max
    final TimeSeries max = maxPrice.generate(ohlcv).get(ZERO);

    // compute Percent-Drawdown Squared
    final double[] percentDrawdownSquared = new double[max.size()];
    for (int p = ZERO, i = maxPrice.lookback(); p < percentDrawdownSquared.length; ++p, ++i) {
      final double percentDrawdown = (ohlcv.close(i) / max.value(p) - ONE) * HUNDRED_PERCENT;
      percentDrawdownSquared[p] = percentDrawdown * percentDrawdown;
    }

    // compute Squared Average
    final double[] squaredAverage = sma(percentDrawdownSquared, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = Math.sqrt(squaredAverage[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
