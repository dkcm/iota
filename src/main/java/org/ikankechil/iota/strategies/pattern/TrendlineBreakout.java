/**
 * TrendlineBreakout.java  v0.7  22 September 2016 9:33:52 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import static org.ikankechil.iota.Signal.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.SimpleBoundedPattern;
import org.ikankechil.iota.indicators.pattern.Trendlines;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.ikankechil.iota.strategies.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trendline breakout strategy.
 *
 * <p>Buys when highs break out from down trendlines (from below)<br>
 * Sells when lows break down from up trendlines (from above)<br>
 *
 * @author Daniel Kuan
 * @version 0.7
 */
public class TrendlineBreakout extends AbstractStrategy {

  private static final Logger logger = LoggerFactory.getLogger(TrendlineBreakout.class);

  /**
   *
   *
   * @param awayPoints
   * @param breakoutThresholdPercentage
   */
  public TrendlineBreakout(final int awayPoints, final double breakoutThresholdPercentage) {
    this(new Trendlines(awayPoints, breakoutThresholdPercentage));
  }

  /**
   *
   *
   * @param awayPoints
   * @param breakoutThresholdPercentage
   * @param runawayThresholdPercentage
   */
  public TrendlineBreakout(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(new Trendlines(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage));
  }

  /**
   *
   *
   * @param trendlines
   */
  public TrendlineBreakout(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   *
   *
   * @param pattern
   */
  public TrendlineBreakout(final SimpleBoundedPattern pattern) {
    super(pattern);
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final int size = ohlcv.size();

    // initialise
    final SignalTimeSeries signals;
    if (lookback >= size) {
      signals = new SignalTimeSeries(toString(), size - ONE);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }
    Arrays.fill(signals.signals(), NONE);
    System.arraycopy(ohlcv.dates(), size - signals.size(), signals.dates(), ZERO, signals.size());

    // generate signals
    generateSignals(signals, ohlcv, indicatorValues);

    return signals;
  }

  private final void generateSignals(final SignalTimeSeries signals,
                                     final OHLCVTimeSeries ohlcv,
                                     final List<List<TimeSeries>> indicatorValues) {
    final List<TimeSeries> trends = indicatorValues.get(ZERO);
    SignalGenerators.BUYER.generateSignals(signals, ohlcv, trends);
    SignalGenerators.SELLER.generateSignals(signals, ohlcv, trends);
  }

  @Override
  protected boolean buy(final double... doubles) {
    return false;
  }

  @Override
  protected boolean sell(final double... doubles) {
    return false;
  }

  private enum SignalGenerators {
    BUYER(BUY, ZERO) {

      @Override
      boolean execute(final double fastYesterday,
                      final double slowYesterday,
                      final double fastToday,
                      final double slowToday) {
        // highs crosses over down trendlines from below
        return crossover(fastYesterday, slowYesterday, fastToday, slowToday);
      }

      @Override
      double[] getFasts(final OHLCVTimeSeries ohlcv) {
        return ohlcv.highs();
      }

    },
    SELLER(SELL, ONE) {

      @Override
      boolean execute(final double fastYesterday,
                      final double slowYesterday,
                      final double fastToday,
                      final double slowToday) {
        // lows crosses under up trendlines from above
        return crossunder(fastYesterday, slowYesterday, fastToday, slowToday);
      }

      @Override
      double[] getFasts(final OHLCVTimeSeries ohlcv) {
        return ohlcv.lows();
      }

    };

    private final Signal signal;
    private final int    slowTrendsTimeSeriesIndex;

    SignalGenerators(final Signal signal, final int slowTrendsTimeSeriesIndex) {
      this.signal = signal;
      this.slowTrendsTimeSeriesIndex = slowTrendsTimeSeriesIndex;
    }

    public void generateSignals(final SignalTimeSeries signals,
                                final OHLCVTimeSeries ohlcv,
                                final List<TimeSeries> trends) {
      final String ohlcvName = ohlcv.toString();

      // prices move faster than their trendlines
      final double[] fasts = getFasts(ohlcv);
      final TrendlineTimeSeries slowTrends = getSlowTrends(trends);
      final List<Trendline> slowTrendlines = slowTrends.trendlines();

      for (final Trendline trendline : slowTrendlines) {
        // locate breakout / breakdown only if trendline is broken
        if (trendline.isBroken()) {
          final int signalIndex = locateSignal(fasts, slowTrends, trendline);
          signals.signal(signal, signalIndex); // set signal

          final String date = signals.date(signalIndex);
          final int c = signalIndex + ohlcv.size() - signals.size();
          final double close = ohlcv.close(c);
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }
    }

    private final int locateSignal(final double[] fasts,
                                   final TimeSeries slowTrends,
                                   final Trendline trendline) {
      // Algorithm:
      // 1. start at trendline's tail and work backwards towards its head,
      //    stopping at the first breakout / breakdown
      // 2. tail is implied breakout / breakdown if none found along trendline

      int today = trendline.x2();         // trendline's tail
      int signalIndex = today;            // tail implied
      double fastToday = fasts[today];
      double slowToday = slowTrends.value(today);

      while (--today > trendline.x1()) {  // trendline's head
        final double fastYesterday = fasts[today];
        final double slowYesterday = slowTrends.value(today);

        if (execute(fastYesterday, slowYesterday, fastToday, slowToday)) {
          signalIndex = today;            // set signal, then stop
          break;
        }

        // shift backwards
        fastToday = fastYesterday;
        slowToday = slowYesterday;
      }

      return signalIndex;
    }

    abstract boolean execute(final double fastYesterday,
                             final double slowYesterday,
                             final double fastToday,
                             final double slowToday);

    abstract double[] getFasts(final OHLCVTimeSeries ohlcv);

    TrendlineTimeSeries getSlowTrends(final List<? extends TimeSeries> trends) {
      return (TrendlineTimeSeries) trends.get(slowTrendsTimeSeriesIndex);
    }

  }

}
