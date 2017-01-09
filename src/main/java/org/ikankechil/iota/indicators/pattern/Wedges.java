/**
 * Wedges.java  v0.1  30 December 2016 12:55:18 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

/**
 * Wedges
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class Wedges extends Triangles { // TODO buggy: Trendlines do not support rising downtrendlines

  /**
   * @param trendlines
   */
  public Wedges(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   * @param trendlines
   * @param ohlcvBarsErrorMargin
   */
  public Wedges(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
    super(trendlines, ohlcvBarsErrorMargin);
  }

}
