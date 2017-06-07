/**
 * Wedges.java  v0.2  30 December 2016 12:55:18 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Wedges
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
abstract class Wedges extends Triangles {

  private final TrendSlopes slope;

  public Wedges(final int awayPoints, final double thresholdPercentage, final TrendSlopes slope, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, slope, slope, endpointVicinity);

    this.slope = slope;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    return slope.isRightDirection(trendline.m()) && // wedge
           !trendline.isPracticallyHorizontal();
  }

//  @Override
//  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
//    return hasIntersect() && hasPotential(counterpart);
//  }

}
