/**
 * DecisionPointTrendModel.java v0.1  31 December 2015 1:14:01 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.Signal.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.trend.EMA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DecisionPoint Trend Analysis
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:trading_strategies:decisionpoint_trend_model
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DecisionPointTrendModel extends AbstractStrategy {

  private static final Logger logger = LoggerFactory.getLogger(DecisionPointTrendModel.class);

  public DecisionPointTrendModel() {
    super(new EMA(5), new EMA(20), new EMA(50), new EMA(200));
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final double[] weekEMA = indicatorValues.get(ZERO).get(ZERO).values();
    final double[] monthEMA = indicatorValues.get(ONE).get(ZERO).values();
    final double[] quarterEMA = indicatorValues.get(TWO).get(ZERO).values();
    final double[] yearEMA = indicatorValues.get(THREE).get(ZERO).values();

    final int size = yearEMA.length;

    int today = (size - ONE - lookback);
    final double weekEMAYesterday = weekEMA[today];
    double monthEMAYesterday = monthEMA[today];
//    double quarterEMAYesterday = quarterEMA[today];
//    double yearEMAYesterday = yearEMA[today];

    // generate signals
    final String ohlcvName = ohlcv.toString();
    final SignalTimeSeries signals = new SignalTimeSeries(toString(), lookback);
    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
         ++today < size;
         ++s, ++c) {
      final String date = ohlcv.date(c);
      final double close = ohlcv.close(c);

      Signal signal = NONE;
      final double weekEMAToday = weekEMA[today];
      final double monthEMAToday = monthEMA[today];
      final double quarterEMAToday = quarterEMA[today];
      final double yearEMAToday = yearEMA[today];

      // long
      if (quarterEMAToday > yearEMAToday) {
        if (monthEMAToday > quarterEMAToday) {
          if (crossover(weekEMAYesterday, monthEMAYesterday, weekEMAToday, monthEMAToday)) {
            signal = BUY;
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }
      }
      // short
      else if (quarterEMAToday < yearEMAToday) {
        if (monthEMAToday < quarterEMAToday) {
          if (crossunder(weekEMAYesterday, monthEMAYesterday, weekEMAToday, monthEMAToday)) {
            signal = SELL;
            logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
          }
        }
      }
      signals.set(date, signal, s);

      monthEMAYesterday = monthEMAToday;
//      quarterEMAYesterday = quarterEMAToday;
//      yearEMAYesterday = yearEMAToday;
    }

    return signals;
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
