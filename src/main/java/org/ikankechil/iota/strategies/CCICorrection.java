/**
 * CCICorrection.java v0.1 8 December 2014 8:55:54 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.strategies.CCICorrection.Bias.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.momentum.CCI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CCI Correction by Donald Lambert
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:cci_correction
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
    super(new CCI(26 * 5), new CCI(26));
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
    final String ohlcvName = ohlcv.toString();

    final double[] longCCI = indicatorValues.get(ZERO).get(ZERO).values();
    final double[] shortCCI = indicatorValues.get(ONE).get(ZERO).values();

    Bias bias = NONE;
    Bias counterTrend = NONE;

    final int size = longCCI.length;
    for (int today = ONE, yesterday = ZERO, c = today + ohlcv.size() - size;
         today < size;
         ++today, ++yesterday, ++c) {
      final String date = ohlcv.date(c);
      final double close = ohlcv.close(c);

      // establish bias
      bias = establishBias(longCCI[yesterday], longCCI[today], bias);

      // look for a smaller counter trend
      switch (bias) {
        case BULLISH:
          // pullback
          if (crossover(shortCCI[today], shortCCI[yesterday], DOWNTREND)) {
            counterTrend = BEARISH;
            continue;
          }
          break;

        case BEARISH:
          // bounce
          if (crossover(shortCCI[yesterday], shortCCI[today], UPTREND)) {
            counterTrend = BULLISH;
            continue;
          }
          break;

        default: // do nothing
          break;
      }

      Signal signal = Signal.NONE;
      // look for the reversal in the counter trend
      // long
      if (bias == BULLISH && counterTrend == BEARISH) {
        if (crossover(shortCCI[yesterday], shortCCI[today], ZERO)) {
          signal = Signal.BUY;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }
      // short
      else if (bias == BEARISH && counterTrend == BULLISH) {
        if (crossover(shortCCI[today], shortCCI[yesterday], ZERO)) {
          signal = Signal.SELL;
          logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
        }
      }
    }

    final SignalTimeSeries signals = null;
    return signals;
  }

  private static final Bias establishBias(final double yesterday,
                                          final double today,
                                          final Bias current) {
    return crossover(yesterday, today, UPTREND)   ? BULLISH :
           crossover(today, yesterday, DOWNTREND) ? BEARISH :
                                                    current;
  }

  @Override
  protected boolean buy(final double... doubles) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected boolean sell(final double... doubles) {
    // TODO Auto-generated method stub
    return false;
  }

}
