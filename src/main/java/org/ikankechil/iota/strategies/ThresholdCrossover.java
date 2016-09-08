/**
 * ThresholdCrossover.java  v0.1  8 September 2016 6:42:08 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
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
 * Signals when an indicator crosses over / under a threshold.
 * <p>
 * Buys when indicator crosses over a threshold<br>
 * Sells when indicator crosses under a threshold<br>
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ThresholdCrossover extends AbstractStrategy {

  private final int           indicatorIndex;

  private final double        buy;
  private final double        sell;

  private static final Logger logger = LoggerFactory.getLogger(ThresholdCrossover.class);

  public ThresholdCrossover(final Indicator indicator, final double threshold) {
    this(indicator, ZERO, threshold);
  }

  public ThresholdCrossover(final Indicator indicator, final int indicatorIndex, final double threshold) {
    this(indicator, indicatorIndex, threshold, threshold);
  }

  public ThresholdCrossover(final Indicator indicator, final double buy, final double sell) {
    this(indicator, ZERO, buy, sell);
  }

  public ThresholdCrossover(final Indicator indicator, final int indicatorIndex, final double buy, final double sell) {
    super(indicator);

    this.indicatorIndex = indicatorIndex;
    this.buy = buy;
    this.sell = sell;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final String ohlcvName = ohlcv.toString();
    final TimeSeries indicator = indicatorValues.get(indicatorIndex).get(ZERO);
    final int size = indicator.size();

    // initialise
    int today;
    final SignalTimeSeries signals;
    if (lookback >= size) {
      today = ZERO;
      signals = new SignalTimeSeries(toString(), size - ONE);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
      today = (size - ONE - lookback);
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }

    // generate signals
    double indicatorYesterday = indicator.value(today);
    for (int s = ZERO, c = (today + ONE + ohlcv.size() - size);
         ++today < size;
         ++s, ++c) {
      final double indicatorToday = indicator.value(today);

      final String date = indicator.date(today);
      final double close = ohlcv.close(c);

      final Signal signal;
      // indicator crosses over a threshold
      if (buy(indicatorYesterday, indicatorToday)) {
        signal = BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      // indicator crosses under a threshold
      else if (sell(indicatorYesterday, indicatorToday)) {
        signal = SELL;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else {
        signal = NONE;
        logger.debug(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      signals.set(date, signal, s);

      // shift forward
      indicatorYesterday = indicatorToday;
    }

    return signals;
  }

  @Override
  protected boolean buy(final double... doubles) {
    return crossover(doubles[ZERO], doubles[ONE], buy);
  }

  @Override
  protected boolean sell(final double... doubles) {
    return crossunder(doubles[ZERO], doubles[ONE], sell);
  }

}
