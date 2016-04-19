/**
 * Strategy.java  v0.2  7 November 2014 7:03:57 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public interface Strategy {

//  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv);

  /**
   * Execute this strategy on the given price-volume data.
   *
   * @param ohlcv price-volume data
   * @param lookback
   * @return a time series of trade signals
   */
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback);

}
