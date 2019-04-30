/**
 * TironeLevels.java  v0.1  21 April 2019 11:18:54 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

/**
 * Tirone Levels by John C. Tirone
 *
 * <p>References:
 * <li>https://www.investopedia.com/terms/t/tironlevels.asp
 * <li>https://www.tradingview.com/script/ZdbzUf9B-Indicator-Tirone-Levels/
 * <li>https://www.metastock.com/customer/resources/taaz/?p=110<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TironeLevels extends AbstractIndicator {

  private final Indicator     min;
  private final Indicator     max;

  private static final String TIRONE_LEVELS_EXTREME_HIGH  = "Tirone Levels Extreme High";
  private static final String TIRONE_LEVELS_REGULAR_HIGH  = "Tirone Levels Regular High";
  private static final String TIRONE_LEVELS_ADJUSTED_MEAN = "Tirone Levels Adjusted Mean";
  private static final String TIRONE_LEVELS_REGULAR_LOW   = "Tirone Levels Regular Low";
  private static final String TIRONE_LEVELS_EXTREME_LOW   = "Tirone Levels Extreme Low";

  public TironeLevels() {
    this(TWENTY);
  }

  public TironeLevels(final int period) {
    super(period, (period - ONE));

    min = new MinimumPrice(period);
    max = new MaximumPrice(period);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    final TimeSeries lowestLows = min.generate(new TimeSeries(EMPTY, ohlcv.dates(), ohlcv.lows()), start).get(ZERO);
    final TimeSeries highestHighs = max.generate(new TimeSeries(EMPTY, ohlcv.dates(), ohlcv.highs()), start).get(ZERO);

    final double[] extremeHighs = new double[ohlcv.size() - lookback + start];
    final double[] regularHighs = new double[extremeHighs.length];
    final double[] adjustedMeans = new double[regularHighs.length];
    final double[] regularLows = new double[adjustedMeans.length];
    final double[] extremeLows = new double[regularLows.length];

    for (int i = start, c = i + lookback; i < adjustedMeans.length; ++i, ++c) {
      final double highestHigh = highestHighs.value(i);
      final double lowestLow = lowestLows.value(i);
      final double adjustedMean = adjustedMeans[i] = (highestHigh + lowestLow + ohlcv.close(c)) * THIRD;

      final double range = highestHigh - lowestLow;
      extremeHighs[i] = adjustedMean + range;
      extremeLows[i] = adjustedMean - range;

      final double twiceAdjustMean = adjustedMean + adjustedMean;
      regularHighs[i] = twiceAdjustMean - lowestLow;
      regularLows[i] = twiceAdjustMean - highestHigh;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(TIRONE_LEVELS_EXTREME_HIGH, dates, extremeHighs),
                         new TimeSeries(TIRONE_LEVELS_REGULAR_HIGH, dates, regularHighs),
                         new TimeSeries(TIRONE_LEVELS_ADJUSTED_MEAN, dates, adjustedMeans),
                         new TimeSeries(TIRONE_LEVELS_REGULAR_LOW, dates, regularLows),
                         new TimeSeries(TIRONE_LEVELS_EXTREME_LOW, dates, extremeLows));
  }

}
