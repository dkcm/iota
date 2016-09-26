/**
 * AbstractStrategy.java  v0.3  10 November 2014 3:51:20 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass for all trading strategies.
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public abstract class AbstractStrategy implements Strategy {

  private final String            name         = getClass().getSimpleName();
  protected final List<Indicator> indicators;

  // Numeric constants
  protected static final int      ZERO         = 0;
  protected static final int      ONE          = 1;
  protected static final int      TWO          = 2;
  protected static final int      THREE        = 3;
  protected static final int      FOUR         = 4;
  protected static final int      FIVE         = 5;
  protected static final int      SIX          = 6;
  protected static final int      SEVEN        = 7;
  protected static final int      EIGHT        = 8;
  protected static final int      NINE         = 9;
  protected static final int      TEN          = 10;
  protected static final int      ELEVEN       = 11;
  protected static final int      TWELVE       = 12;
  protected static final int      THIRTEEN     = 13;
  protected static final int      FOURTEEN     = 14;
  protected static final int      FIFTEEN      = 15;
  protected static final int      SIXTEEN      = 16;
  protected static final int      SEVENTEEN    = 17;
  protected static final int      EIGHTEEN     = 18;
  protected static final int      NINETEEN     = 19;
  protected static final int      TWENTY       = 20;
  protected static final int      TWENTY_ONE   = 21;
  protected static final int      TWENTY_TWO   = 22;
  protected static final int      TWENTY_FIVE  = 25;
  protected static final int      TWENTY_SIX   = 26;
  protected static final int      TWENTY_EIGHT = 28;
  protected static final int      THIRTY       = 30;
  protected static final int      THIRTY_TWO   = 32;
  protected static final int      THIRTY_THREE = 33;
  protected static final int      THIRTY_FOUR  = 34;
  protected static final int      THIRTY_FIVE  = 35;
  protected static final int      FORTY        = 40;
  protected static final int      FORTY_FIVE   = 45;
  protected static final int      FIFTY        = 50;
  protected static final int      SIXTY        = 60;
  protected static final int      SEVENTY      = 70;

  protected static final String   SPACE        = " ";

  private static final Logger     logger       = LoggerFactory.getLogger(AbstractStrategy.class);

  public AbstractStrategy(final Indicator... indicators) {
    this.indicators = Arrays.asList(indicators);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return execute(ohlcv, MAX_LOOKBACK);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    if (lookback < ZERO) {
      throw new IllegalArgumentException("Negative lookback: " + lookback);
    }
    logger.info("Executing strategy {} on {}", toString(), ohlcv);

    final List<List<TimeSeries>> indicatorValues = generateIndicatorValues(ohlcv);
    return generateSignals(ohlcv, indicatorValues, lookback);
  }

  protected List<List<TimeSeries>> generateIndicatorValues(final OHLCVTimeSeries ohlcv) {
    // 3 kinds of lookback:
    // 1. indicator (number of periods required for 1 data point)
    // 2. strategy
    // 3. "window of interest"

    final List<List<TimeSeries>> indicatorValues = new ArrayList<>(indicators.size());
    int maxLookback = ZERO;
    for (final Indicator indicator : indicators) {
      indicatorValues.add(indicator.generate(ohlcv));
      maxLookback = Math.max(indicator.lookback(), maxLookback);
    }

    // truncate to cater to different indicator sizes
    truncate(indicatorValues, ohlcv.size() - maxLookback);

    return indicatorValues;
  }

  protected abstract SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                                      final List<List<TimeSeries>> indicatorValues,
                                                      final int lookback);

  protected abstract boolean buy(final double... doubles);

  protected abstract boolean sell(final double... doubles);

  protected static final void truncate(final Collection<List<TimeSeries>> indicatorValues,
                                       final int shortestLength) {
    for (final List<TimeSeries> values : indicatorValues) {
      final ListIterator<TimeSeries> iterator = values.listIterator();
      while (iterator.hasNext()) {
        final TimeSeries series = iterator.next();
        final int size = series.size();
        // truncate
        if (size > shortestLength) {
          final int offset = size - shortestLength;
          iterator.set(new TimeSeries(series.toString(),
                                      Arrays.copyOfRange(series.dates(),
                                                         offset,
                                                         size),
                                      Arrays.copyOfRange(series.values(),
                                                         offset,
                                                         size)));
          logger.debug("Indicator truncated: {} (size: {} -> {})",
                       series,
                       size,
                       shortestLength);
        }
      }
    }
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   * Values cross over a threshold.
   *
   * @param yesterday
   * @param today
   * @param threshold
   * @return
   */
  protected static final boolean crossover(final double yesterday,
                                           final double today,
                                           final double threshold) {
    // values cross over a threshold
    return (yesterday < threshold) && (today > threshold);
  }

  /**
   * Values cross under a threshold.
   *
   * @param yesterday
   * @param today
   * @param threshold
   * @return
   */
  protected static final boolean crossunder(final double yesterday,
                                            final double today,
                                            final double threshold) {
    // values cross under a threshold
    return (yesterday > threshold) && (today < threshold);
  }

  /**
   * The fast time series crosses over the slower one.
   *
   * @param fastYesterday
   * @param slowYesterday
   * @param fastToday
   * @param slowToday
   * @return
   */
  protected static final boolean crossover(final double fastYesterday,
                                           final double slowYesterday,
                                           final double fastToday,
                                           final double slowToday) {
    // 1-value lookback
    return (fastYesterday < slowYesterday) && (fastToday > slowToday);
  }

  /**
   * The fast time series crosses under the slower one.
   *
   * @param fastYesterday
   * @param slowYesterday
   * @param fastToday
   * @param slowToday
   * @return
   */
  protected static final boolean crossunder(final double fastYesterday,
                                            final double slowYesterday,
                                            final double fastToday,
                                            final double slowToday) {
    // 1-value lookback
    return (fastYesterday > slowYesterday) && (fastToday < slowToday);
  }
  // TODO support 2-value lookback when determining buy / sell signal?

}
