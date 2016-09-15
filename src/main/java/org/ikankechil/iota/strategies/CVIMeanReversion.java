/**
 * CVIMeanReversion.java	v0.1	16 September 2016 1:07:14 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.SignalTimeSeries;
import org.ikankechil.iota.indicators.momentum.CVI;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CVIMeanReversion implements Strategy {

  private final Strategy      meanReversion;

  // thresholds
  private static final double OVERSOLD   = -8.0;
  private static final double OVERBOUGHT = 8.0;

  public CVIMeanReversion(final int period) {
    this(period, OVERSOLD, OVERBOUGHT);
  }

  public CVIMeanReversion(final int period, final double oversold, final double overbought) {
    meanReversion = new ThresholdCrossover(new CVI(period),
                                           oversold,
                                           overbought);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv) {
    return meanReversion.execute(ohlcv);
  }

  @Override
  public SignalTimeSeries execute(final OHLCVTimeSeries ohlcv, final int lookback) {
    return meanReversion.execute(ohlcv, lookback);
  }

}
