/**
 * KaufmanVolatilityStops.java  v0.2  18 October 2016 7:15:56 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Kaufman's Volatility Stops by Perry Kaufman
 *
 * <p>http://thepatternsite.com/stops.html#VS<br>
 * http://bookzz.org/book/938775/4bc6bf<br>
 * http://www.tradingsetupsreview.com/ultimate-guide-volatility-stop-losses/<br>
 * http://bmoen.com/stock-stops/stock-volatility-stop.aspx<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KaufmanVolatilityStops extends AbstractIndicator {

  private final double        multiplier;
  private final double        inversePeriod;

  private static final String SHORT_KAUFMAN_VOLATILITY_STOPS = "Short Kaufman Volatility Stops";
  private static final String LONG_KAUFMAN_VOLATILITY_STOPS  = "Long Kaufman Volatility Stops";

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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // 1. Compute the average daily high-low price range for the prior month
    // 2. Multiply by 2 to get volatility
    // 3. Subtract volatility from the current low price to get long stops
    //    Add volatility to the current high price to get short stops
    // 4. Recalculate long stop volatility if price makes a new high
    //    Recalculate short stop volatility if price makes a new low

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] shortStops = new double[size - lookback];
    final double[] longStops = new double[shortStops.length];

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    // compute indicator
    int i = ZERO;
    int j = i + lookback;

    final double initialVolatility = computeVolatility(i, highs, lows);
    double longStop = computeLongStop(lows[j], initialVolatility);
    double shortStop = computeShortStop(highs[j], initialVolatility);
    longStops[i] = longStop;
    shortStops[i] = shortStop;
    double max = max(highs, i, ++j);
    double min = min(lows, i, j);

    for (; ++i < longStops.length; ++j) {
      final double high = highs[j];
      final double low = lows[j];
      if (high > max) {
        max = high;
        longStop = computeLongStop(low, computeVolatility(i, highs, lows));
      }
      if (low < min) {
        min = low;
        shortStop = computeShortStop(high, computeVolatility(i, highs, lows));
      }
      longStops[i] = longStop;
      shortStops[i] = shortStop;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(SHORT_KAUFMAN_VOLATILITY_STOPS, dates, shortStops),
                         new TimeSeries(LONG_KAUFMAN_VOLATILITY_STOPS, dates, longStops));
  }

  private final double computeVolatility(final int start, final double[] highs, final double[] lows) {
    double averageRange = ZERO;
    for (int i = start; i < start + period; ++i) {
      averageRange += (highs[i] - lows[i]); // range
    }
    averageRange *= inversePeriod;          // average daily range
    return (averageRange * multiplier);     // volatility
  }

  private static final double computeLongStop(final double low, final double volatility) {
    return low - volatility;
  }

  private static final double computeShortStop(final double high, final double volatility) {
    return high + volatility;
  }

}
