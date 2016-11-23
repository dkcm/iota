/**
 * TrendlineBreakout.java  v0.3  22 September 2016 9:33:52 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import static org.ikankechil.iota.Signal.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
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
 * @version 0.3
 */
public class TrendlineBreakout extends AbstractStrategy {

  private final Trendlines    trends;

  private static final Logger logger = LoggerFactory.getLogger(TrendlineBreakout.class);

  public TrendlineBreakout(final int awayPoints, final double thresholdPercentage) {
    this(new Trendlines(awayPoints, thresholdPercentage));
  }

  public TrendlineBreakout(final Trendlines trendlines) {
    super(trendlines);

    trends = trendlines;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    final List<TimeSeries> trendlines = indicatorValues.get(ZERO);
    final double[] downTrendlines = trendlines.get(ZERO).values();
    final double[] upTrendlines = trendlines.get(ONE).values();

    final List<Trendline> downTrends = trends.downTrendlines();
    final List<Trendline> upTrends = trends.upTrendlines();

    final int size = ohlcv.size();

    // initialise
//    int today;
    final SignalTimeSeries signals;
    if (lookback >= size) {
//      today = ZERO;
      signals = new SignalTimeSeries(toString(), size - ONE);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
//      today = (size - ONE - lookback);
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }
    Arrays.fill(signals.signals(), NONE);
    System.arraycopy(ohlcv.dates(), size - signals.size(), signals.dates(), ZERO, signals.size());

    // generate signals
    for (final Trendline trend : downTrends) {
      int today = trend.x2();
      double highToday = highs[today];
      double downToday = downTrendlines[today];

      while (--today >= trend.x1()) {
        final double highYesterday = highs[today];
        final double downYesterday = downTrendlines[today];

        // highs crosses over down trendlines from below
        if (buy(highYesterday, downYesterday, highToday, downToday)) {
          signals.signal(BUY, today);
        }

        // shift backwards
        highToday = highYesterday;
        downToday = downYesterday;
      }
    }

    for (final Trendline trend : upTrends) {
      int today = trend.x2();
      double lowToday = lows[today];
      double upToday = upTrendlines[today];

      while (--today >= trend.x1()) {
        final double lowYesterday = lows[today];
        final double upYesterday = upTrendlines[today];

        // lows crosses under up trendlines from above
        if (sell(lowYesterday, upYesterday, lowToday, upToday)) {
          signals.signal(SELL, today);
        }

        // shift backwards
        lowToday = lowYesterday;
        upToday = upYesterday;
      }
    }

    return signals;
  }

//  @Override
//  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
//                                             final List<List<TimeSeries>> indicatorValues,
//                                             final int lookback) {
//    final String ohlcvName = ohlcv.toString();
//
//    final double[] highs = ohlcv.highs();
//    final double[] lows = ohlcv.lows();
//
//    final List<TimeSeries> trendlines = indicatorValues.get(ZERO);
//    final double[] downTrendlines = trendlines.get(ZERO).values();
//    final double[] upTrendlines = trendlines.get(ONE).values();
//
//    final int size = ohlcv.size();
//
//    // initialise
//    int today;
//    final SignalTimeSeries signals;
//    if (lookback >= size) {
//      today = ZERO;
//      signals = new SignalTimeSeries(toString(), size - ONE);
//      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
//    }
//    else {
//      today = (size - ONE - lookback);
//      signals = new SignalTimeSeries(toString(), lookback);
//      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
//    }
//
//    double highYesterday = highs[today];
//    double lowYesterday = lows[today];
//    double downYesterday = downTrendlines[today];
//    double upYesterday = upTrendlines[today];
//
//    // generate signals
//    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
//         ++today < size;
//         ++s, ++c) {
//      final double highToday = highs[today];
//      final double lowToday = lows[today];
//      final double downToday = downTrendlines[today];
//      final double upToday = upTrendlines[today];
//
//      final String date = ohlcv.date(today);
//      final double close = ohlcv.close(c);
//
//      final Signal signal;
//      // highs crosses over down trendlines from below
//      if (buy(highYesterday, downYesterday, highToday, downToday)) {
//        signal = BUY;
//        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
//      }
//      // lows crosses under up trendlines from above
//      else if (sell(lowYesterday, upYesterday, lowToday, upToday)) {
//        signal = SELL;
//        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
//      }
//      else {
//        signal = NONE;
//        logger.debug(TRADE_SIGNAL, signal, ohlcvName, date, close);
//      }
//      signals.set(date, signal, s);
//
//      // shift forward
//      highYesterday = highToday;
//      lowYesterday = lowToday;
//      downYesterday = downToday;
//      upYesterday = upToday;
//    }
//
//    return signals;
//  }

  @Override
  protected boolean buy(final double... doubles) {
    return crossover(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

  @Override
  protected boolean sell(final double... doubles) {
    return crossunder(doubles[ZERO], doubles[ONE], doubles[TWO], doubles[THREE]);
  }

}
