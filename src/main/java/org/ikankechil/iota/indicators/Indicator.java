/**
 * Indicator.java  v0.3  7 November 2014 7:01:28 PM
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
 * @version 0.3
 */
public interface Indicator extends Comparable<Indicator> {

  /**
   * Generate indicator values from prices and volumes.
   *
   * @param ohlcv <code>TimeSeries</code> of prices (open, high, low and close)
   *          and volumes
   * @return indicator values as a <code>List</code> of <code>TimeSeries</code>
   */
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv);

  /**
   * Generate indicator values from prices and volumes starting from a given
   * point in time.
   *
   * @param ohlcv <code>TimeSeries</code> of prices (open, high, low and close)
   *          and volumes
   * @param start index of the starting point in time
   * @return indicator values as a <code>List</code> of <code>TimeSeries</code>
   */
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start);

  /**
   * Generate indicator values from prices only.
   *
   * @param series <code>TimeSeries</code> of prices only (open / high / low /
   *          close)
   * @return indicator values as a <code>List</code> of <code>TimeSeries</code>
   */
  public List<TimeSeries> generate(final TimeSeries series);

  /**
   * Generate indicator values from prices only starting from a given point in
   * time.
   *
   * @param series <code>TimeSeries</code> of prices only (open / high / low /
   *          close)
   * @param start index of the starting point in time
   * @return indicator values as a <code>List</code> of <code>TimeSeries</code>
   */
  public List<TimeSeries> generate(final TimeSeries series, final int start);

  /**
   * Look-back required by this <code>Indicator</code>.
   *
   * @return
   */
  public int lookback();

}
