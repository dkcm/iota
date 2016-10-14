/**
 * CCICorrection.java  v0.1  8 December 2014 8:55:54 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.momentum;

import static org.ikankechil.iota.strategies.momentum.CCICorrection.Bias.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.momentum.CCI;
import org.ikankechil.iota.strategies.AbstractStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CCI Correction by Donald Lambert
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:cci_correction<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class CCICorrection extends AbstractStrategy {

  // thresholds
  private static final int    UPTREND   = 100;
  private static final int    DOWNTREND = -100;

  private static final Logger logger    = LoggerFactory.getLogger(CCICorrection.class);

  public CCICorrection() {
    super(new CCI(TWENTY_SIX * FIVE), new CCI(TWENTY_SIX));
//    super(new CCI(200), new CCI(5));
  }

  enum Bias {
    BULLISH, BEARISH, NONE;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    //
    //
    //

    final TimeSeries longCCI = indicatorValues.get(ZERO).get(ZERO);
    final TimeSeries shortCCI = indicatorValues.get(ONE).get(ZERO);

    Bias bias = NONE;
    Bias counterTrend = NONE;

    final int size = longCCI.size();
    int today = ZERO;
    double longCCIYesterday = longCCI.value(today);
    double shortCCIYesterday = shortCCI.value(today);

    for (int c = ohlcv.size() - size; ++today < size; ++c) {
      final String date = ohlcv.date(c);
      final double close = ohlcv.close(c);

      // establish bias
      final double longCCIToday = longCCI.value(today);
      bias = establishBias(longCCIYesterday, longCCIToday, bias);

      // look for a smaller counter trend
      final double shortCCIToday = shortCCI.value(today);
      switch (bias) {
        case BULLISH:
          // pullback
          if (buy(shortCCIToday, shortCCIYesterday, DOWNTREND)) {
            counterTrend = BEARISH;
            continue;
          }
          break;

        case BEARISH:
          // bounce
          if (sell(shortCCIToday, shortCCIYesterday, UPTREND)) {
            counterTrend = BULLISH;
            continue;
          }
          break;

        default: // do nothing
          break;
      }

      final String ohlcvName = ohlcv.toString();
      Signal signal = Signal.NONE;
      // look for the reversal in the counter trend
      // long
      if (bias == BULLISH && counterTrend == BEARISH) {
        if (buy(shortCCIYesterday, shortCCIToday, ZERO)) {
          signal = Signal.BUY;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }
      // short
      else if (bias == BEARISH && counterTrend == BULLISH) {
        if (sell(shortCCIYesterday, shortCCIToday, ZERO)) {
          signal = Signal.SELL;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }

      // shift forward in time
      longCCIYesterday = longCCIToday;
      shortCCIYesterday = shortCCIToday;
    }

    final SignalTimeSeries signals = null;
    return signals;
  }

  private final Bias establishBias(final double yesterday,
                                   final double today,
                                   final Bias current) {
    return buy(yesterday, today, UPTREND)    ? BULLISH :
           sell(yesterday, today, DOWNTREND) ? BEARISH :
                                               current;
  }

  @Override
  protected boolean buy(final double... doubles) {
    return crossover(doubles[ZERO], doubles[ONE], doubles[TWO]);
  }

  @Override
  protected boolean sell(final double... doubles) {
    return crossunder(doubles[ZERO], doubles[ONE], doubles[TWO]);
  }

}
