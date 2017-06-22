/**
 * Triangles.java  v0.4  8 January 2016 10:56:34 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;

/**
 * Triangles pattern recognition algorithm.  Covers triangles and wedges.
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public abstract class Triangles extends SimpleBoundedPattern {

  public Triangles(final int awayPoints, final double thresholdPercentage, final TrendSlopes upper, final TrendSlopes lower) {
    this(awayPoints, thresholdPercentage, upper, lower, ENDPOINT_VICINITY);
  }

  /**
   *
   *
   *
   * @param awayPoints
   * @param thresholdPercentage
   * @param upper
   * @param lower
   * @param endpointVicinity number of OHLCV bars the heads and tails of the
   *          trendlines forming the triangle must be in the vicinity of
   */
  public Triangles(final int awayPoints, final double thresholdPercentage, final TrendSlopes upper, final TrendSlopes lower, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, upper, lower, endpointVicinity);
  }

}
