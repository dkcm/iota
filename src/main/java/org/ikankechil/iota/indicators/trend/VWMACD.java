/**
 * VWMACD.java  0.1  19 December 2016 2:19:38 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.HashMap;
import java.util.Map;

import org.ikankechil.iota.OHLCVTimeSeries;

/**
 * Volume-Weighted Moving Average Convergence/Divergence (MACD) by David Hawkins
 *
 * <p>http://traders.com/Documentation/FEEDbk_docs/2009/10/Hawkins.html<br>
 * http://traders.com/Documentation/FEEDbk_docs/2009/10/TradersTips.html<br>
 * <br>
 * <br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWMACD extends MACD {

  private static final Map<Integer, VWEMA> MOVING_AVERAGES = new HashMap<>();

  public VWMACD() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public VWMACD(final int fast, final int slow, final int signal) {
    super(fast, slow, signal);
  }

  @Override
  double[] smoothenPrices(final int smoothingPeriod, final OHLCVTimeSeries ohlcv) {
    VWEMA vwema = MOVING_AVERAGES.get(smoothingPeriod);
    if (vwema == null) {
      MOVING_AVERAGES.put(smoothingPeriod, vwema = new VWEMA(smoothingPeriod));
    }
    return vwema.generate(ohlcv).get(ZERO).values();
  }

}
