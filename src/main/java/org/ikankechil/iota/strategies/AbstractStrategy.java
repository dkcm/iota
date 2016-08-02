/**
 * AbstractStrategy.java  v0.2  10 November 2014 3:51:20 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
 * @version 0.2
 */
public abstract class AbstractStrategy implements Strategy {

  private final String            name          = getClass().getSimpleName();
  protected final List<Indicator> indicators; // TODO enums can implement interfaces

  protected static final int      ZERO          = 0;
  protected static final int      ONE           = 1;
  protected static final int      TWO           = 2;
  protected static final int      THREE         = 3;
  protected static final int      FOUR          = 4;
  protected static final int      FIVE          = 5;
  protected static final int      SIX           = 6;
  protected static final int      SEVEN         = 7;
  protected static final int      EIGHT         = 8;
  protected static final int      NINE          = 9;
  protected static final int      TEN           = 10;
  protected static final int      ELEVEN        = 11;
  protected static final int      TWELVE        = 12;
  protected static final int      THIRTEEN      = 13;
  protected static final int      FOURTEEN      = 14;

  protected static final String   SIGNAL_OCCURS = "Signal: {} {} (Date: {}, Close: {})";

  private static final Logger     logger        = LoggerFactory.getLogger(AbstractStrategy.class);

  public AbstractStrategy(final Indicator... indicators) {
    this.indicators = Arrays.asList(indicators);
  }

  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return execute(ohlcv, Integer.MAX_VALUE);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    if (lookback < ZERO) {
      throw new IllegalArgumentException("Negative lookback");
    }
    logger.info("Executing strategy {} for: {}", name, ohlcv);

    final Map<Indicator, List<TimeSeries>> indicatorValues = generateIndicatorValues(ohlcv);
    return generateSignals(ohlcv, indicatorValues, lookback);
    // TODO chain conditions up to leverage && and || "short-circuit" logic
  }

  protected Map<Indicator, List<TimeSeries>> generateIndicatorValues(final OHLCVTimeSeries ohlcv) {
    final Map<Indicator, List<TimeSeries>> indicatorValues = new LinkedHashMap<>(indicators.size());
    int maxLookback = ZERO;
    for (final Indicator indicator : indicators) {
      indicatorValues.put(indicator, indicator.generate(ohlcv));
      maxLookback = Math.max(indicator.lookback(), maxLookback);
    }

    // truncate to cater to different indicator sizes
    truncate(indicatorValues, ohlcv.size() - maxLookback);

    return indicatorValues;
  }

  protected abstract SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                                      final Map<Indicator, List<TimeSeries>> indicatorValues,
                                                      final int lookback);

  protected static final void truncate(final Map<Indicator, List<TimeSeries>> indicatorValues,
                                       final int shortestLength) {
    for (final List<TimeSeries> values : indicatorValues.values()) {
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
   * @param less
   * @param more
   * @param threshold
   * @return
   */
  protected static final boolean crossover(final double less,
                                           final double more,
                                           final double threshold) {
    // values cross over a threshold
    return (less < threshold) && (more > threshold);
  }

  /**
   * Values cross under a threshold.
   *
   * @param less
   * @param more
   * @param threshold
   * @return
   */
  protected static final boolean crossunder(final double less,
                                            final double more,
                                            final double threshold) {
    // values cross under a threshold
    return (less > threshold) && (more < threshold);
  }

  /**
   * A fast time series crosses over a slower one.
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
   * A fast time series crosses under a slower one.
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
  } // TODO support 2-value lookback when determining buy / sell signal?

}
