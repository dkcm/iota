/**
 * MCVIReversal.java  v0.1  16 September 2016 12:17:58 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.indicators.momentum.MCVI;

/**
 *
 * <p>
 * https://traderedge.net/2013/01/19/modified-chartmill-value-indicator-mcvi/
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MCVIReversal implements Strategy {

  private final Strategy      reversal;

  // thresholds
  private static final double OVERSOLD   = -0.51;
  private static final double OVERBOUGHT = 0.43;

  public MCVIReversal() {
    this(3);
  }

  public MCVIReversal(final int period) {
    this(period, OVERSOLD, OVERBOUGHT);
  }

  public MCVIReversal(final int period, final double oversold, final double overbought) {
    reversal = new ThresholdCrossover(new MCVI(period),
                                      oversold,
                                      overbought);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return reversal.execute(ohlcv);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    return reversal.execute(ohlcv, lookback);
  }

}
