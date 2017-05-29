/**
 * RightAngledTriangles.java  0.2  23 December 2016 12:49:31 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Abstract class representing ascending and descending triangles.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public abstract class RightAngledTriangles extends Triangles {

  /**
   *
   *
   * @param trendlines
   */
  public RightAngledTriangles(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin number of OHLCV bars the heads and tails of the
   *          trendlines forming the triangle must be in the vicinity of
   */
  public RightAngledTriangles(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
    super(trendlines, ohlcvBarsErrorMargin);
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    // search for complementary counterpart only when trendline is flat
    return trendline.isPracticallyHorizontal();
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    return !hasPotential(counterpart) &&
           super.isComplementary(candidate, counterpart);
  }

}
