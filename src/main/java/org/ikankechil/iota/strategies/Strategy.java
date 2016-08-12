/**
 * Strategy.java  v0.3  7 November 2014 7:03:57 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;

/**
 * Interface for for all trading strategies.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public interface Strategy {

  public static final int    MAX_LOOKBACK = Integer.MAX_VALUE;

  public static final String TRADE_SIGNAL = "Signal: {} {} (Date: {}, Close: {})";

  /**
   * Execute this strategy on the given prices and volumes.
   *
   * @param ohlcv <code>OHLCVTimeSeries</code> of prices (open, high, low and
   *          close) and volumes
   * @return a time series of trade signals
   */
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv);

  /**
   * Execute this strategy on the given prices and volumes over a duration of
   * interest.
   *
   * @param ohlcv <code>OHLCVTimeSeries</code> of prices (open, high, low and
   *          close) and volumes
   * @param lookback duration of interest
   * @return a time series of trade signals
   */
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback);

}
