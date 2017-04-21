/**
 * Threshold.java  v0.1  14 April 2017 9:03:01 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import static org.ikankechil.iota.Signal.*;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.Signal;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Threshold strategy.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Threshold extends AbstractStrategy {

  private final double        buy;
  private final double        sell;

  private static final Logger logger = LoggerFactory.getLogger(Threshold.class);

  public Threshold(final Indicator indicator, final double threshold) {
    this(indicator, threshold, threshold);
  }

  public Threshold(final Indicator indicator, final double buy, final double sell) {
    super(indicator);

    this.buy = buy;
    this.sell = sell;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final String ohlcvName = ohlcv.toString();

    final TimeSeries indicator = indicatorValues.get(ZERO).get(ZERO);

    final int size = indicator.size();

    // initialise
    int today;
    final SignalTimeSeries signals;
    if (lookback >= size) {
      today = ZERO;
      signals = new SignalTimeSeries(toString(), size);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
      today = (size - lookback);
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }

    // generate signals
    for (int s = ZERO, c = (today + ohlcv.size() - size);
         today < size;
         ++today, ++s, ++c) {
      final double indicatorValue = indicator.value(today);
      final String date = indicator.date(today);
      final double close = ohlcv.close(c);

      final Signal signal;
      if (buy(indicatorValue)) {
        signal = BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else if (sell(indicatorValue)) {
        signal = SELL;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else {
        signal = NONE;
        logger.debug(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      signals.set(date, signal, s);
    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    return doubles[ZERO] < buy;
  }

  @Override
  protected boolean sell(final double... doubles) {
    return doubles[ZERO] > sell;
  }

}
