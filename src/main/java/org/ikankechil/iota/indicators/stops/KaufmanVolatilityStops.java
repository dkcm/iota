/**
 * KaufmanVolatilityStops.java  v0.1  18 October 2016 7:15:56 pm
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kaufman's Volatility Stops by Perry Kaufman
 *
 * <p>http://thepatternsite.com/stops.html#VS<br>
 * http://bookzz.org/book/938775/4bc6bf<br>
 * http://www.tradingsetupsreview.com/ultimate-guide-volatility-stop-losses/<br>
 * http://bmoen.com/stock-stops/stock-volatility-stop.aspx<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KaufmanVolatilityStops extends AbstractIndicator {

  private final double multiplier;
  private final double inversePeriod;

  public KaufmanVolatilityStops() {
    this(TWENTY_TWO, TWO);
  }

  public KaufmanVolatilityStops(final int period, final double multiplier) {
    super(period, period - ONE);
    throwExceptionIfNegative(multiplier);

    this.multiplier = multiplier;
    inversePeriod = ONE / (double) period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Compute the average daily high-low price range for the prior month
    // 2. Multiply by 2 to get volatility
    // 3. Subtract volatility from the current low price to get stops
    // 4. Recalculate volatility if price makes a new high

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    // compute indicator
    int i = ZERO;
    int j = i + lookback;
    double stop = lows[j] - computeVolatility(i, highs, lows);
    output[i] = stop;
    double max = max(highs, i, ++j);

    for (; ++i < output.length; ++j) {
      final double high = highs[j];
      if (high > max) {
        max = high;
        stop = lows[j] - computeVolatility(i, highs, lows);
      }
      output[i] = stop;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final double computeVolatility(final int start, final double[] highs, final double[] lows) {
    double averageRange = ZERO;
    for (int i = start; i < start + period; ++i) {
      averageRange += (highs[i] - lows[i]); // range
    }
    averageRange *= inversePeriod;          // average daily range
    return (averageRange * multiplier);     // volatility
  }

}
