/**
 * TrendlineBreakout.java  v0.4  22 September 2016 9:33:52 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import static org.ikankechil.iota.Signal.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
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
 * @version 0.4
 */
public class TrendlineBreakout extends AbstractStrategy {

  private final Trendlines      trendlines;

  private final SignalGenerator buyer  = new SignalGenerator(BUY) {
    @Override
    boolean execute(final double fastYesterday,
                    final double slowYesterday,
                    final double fastToday,
                    final double slowToday) {
      // highs crosses over down trendlines from below
      return buy(fastYesterday, slowYesterday, fastToday, slowToday);
    }
  };
  private final SignalGenerator seller = new SignalGenerator(SELL) {
    @Override
    boolean execute(final double fastYesterday,
                    final double slowYesterday,
                    final double fastToday,
                    final double slowToday) {
      // lows crosses under up trendlines from above
      return sell(fastYesterday, slowYesterday, fastToday, slowToday);
    }
  };

  private static final Logger   logger = LoggerFactory.getLogger(TrendlineBreakout.class);

  public TrendlineBreakout(final int awayPoints, final double thresholdPercentage) {
    this(new Trendlines(awayPoints, thresholdPercentage));
  }

  public TrendlineBreakout(final Trendlines trendlines) {
    super(trendlines);

    this.trendlines = trendlines;
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
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    final List<TimeSeries> trends = indicatorValues.get(ZERO);
    final TimeSeries downTrends = trends.get(ZERO);
    final TimeSeries upTrends = trends.get(ONE);

    final List<Trendline> downTrendlines = trendlines.downTrendlines();
    final List<Trendline> upTrendlines = trendlines.upTrendlines();

    buyer.generateSignals(signals, highs, downTrends, downTrendlines);
    seller.generateSignals(signals, lows, upTrends, upTrendlines);
  }

  @Override
  protected boolean buy(final double... doubles) {
    return crossover(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

  @Override
  protected boolean sell(final double... doubles) {
    return crossunder(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

  private abstract static class SignalGenerator {

    private final Signal signal;

    SignalGenerator(final Signal signal) {
      this.signal = signal;
    }

    public void generateSignals(final SignalTimeSeries signals,
                                final double[] fasts,
                                final TimeSeries slowTrends,
                                final List<Trendline> slowTrendlines) {
      for (final Trendline trendline : slowTrendlines) {
        // start at trendline's tail and work backwards towards its head,
        // stopping at the first breakout / breakdown

        int today = trendline.x2();
        double fastToday = fasts[today];
        double slowToday = slowTrends.value(today);

        while (--today >= trendline.x1()) {
          final double fastYesterday = fasts[today];
          final double slowYesterday = slowTrends.value(today);

          if (execute(fastYesterday, slowYesterday, fastToday, slowToday)) {
            signals.signal(signal, today);
            break;
          }

          // shift backwards
          fastToday = fastYesterday;
          slowToday = slowYesterday;
        }
      }
    }

    abstract boolean execute(final double fastYesterday,
                             final double slowYesterday,
                             final double fastToday,
                             final double slowToday);

  }

}
