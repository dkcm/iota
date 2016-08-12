/**
 * DivergenceStrategy.java  v0.1  4 August 2016 5:51:21 pm
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
import org.ikankechil.iota.indicators.pattern.Divergence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>
 * Buy on positive trough divergence<br>
 * Sell on positive peak divergence<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DivergenceStrategy extends AbstractStrategy {

  private final double        threshold;

  private static final Logger logger = LoggerFactory.getLogger(DivergenceStrategy.class);

  public DivergenceStrategy(final Indicator indicator, final int awayPoints) {
    this(new Divergence(indicator, awayPoints));
  }

  public DivergenceStrategy(final Divergence divergence) {
    this(divergence, (double) ZERO);
  }

  public DivergenceStrategy(final Divergence divergence, final double threshold) {
    super(divergence);
    if (threshold < ZERO) {
      throw new IllegalArgumentException();
    }
    this.threshold = threshold;
  }

  @Override
  protected SignalTimeSeries generateSignals(final OHLCVTimeSeries ohlcv,
                                             final List<List<TimeSeries>> indicatorValues,
                                             final int lookback) {
    final String ohlcvName = ohlcv.toString();

    // peak and trough divergences
    final List<TimeSeries> divergences = indicatorValues.get(ZERO);
    final TimeSeries peaks = divergences.get(ZERO);
    final TimeSeries troughs = divergences.get(ONE);

    final int size = peaks.size();

    // initialise
    int today;
    final SignalTimeSeries signals;
    if (lookback >= size) {
      today = ZERO;
      signals = new SignalTimeSeries(toString(), size /*- ONE*/);
      logger.debug("Lookback ({}) >= indicator size ({})", lookback, size);
    }
    else {
      today = (size /*- ONE*/ - lookback);
      signals = new SignalTimeSeries(toString(), lookback);
      logger.debug("Lookback ({}) < indicator size ({})", lookback, size);
    }

    // generate signals
    for (int s = ZERO, c = (today /*+ ONE + ohlcv.size() - size*/); // TODO sort out
         today < size;
         ++today, ++s, ++c) {
      final double peak = peaks.value(today);
      final double trough = troughs.value(today);

      final String date = peaks.date(today);
      final double close = ohlcv.close(c);

      final Signal signal;
      if (buy(trough)) {
        signal = BUY;
        logger.info(TRADE_SIGNAL, signal, ohlcvName, date, close);
      }
      else if (sell(peak)) {
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
    return doubles[ZERO] > threshold;
  }

  @Override
  protected boolean sell(final double... doubles) {
    return doubles[ZERO] > threshold;
  }

}
