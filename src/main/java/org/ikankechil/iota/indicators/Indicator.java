/**
 * Indicator.java  v0.2  7 November 2014 7:01:28 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Adapter design pattern for TA-Lib.
 * <p>
 * <a href="http://ta-lib.org/">http://ta-lib.org/</a>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public interface Indicator extends Comparable<Indicator> { // TODO v0.2 support updates

  /**
   * Generate indicator values from prices and volumes.
   *
   * @param ohlcv
   *          <code>TimeSeries</code> of prices (open-high-low-close) and
   *          volumes
   * @return
   */
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv);

  /**
   * Generate indicator values from prices only.
   *
   * @param series
   * @return
   */
  public List<TimeSeries> generate(final TimeSeries series);

  /**
   * Look-back required by this <code>Indicator</code>.
   *
   * @return
   */
  public int lookback();

}
