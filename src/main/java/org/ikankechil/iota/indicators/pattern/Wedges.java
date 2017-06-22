/**
 * Wedges.java  v0.3  30 December 2016 12:55:18 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Wedges
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:rising_wedge_reversal<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:falling_wedge_reversal<br>
 * <li>http://thepatternsite.com/risewedge.html<br>
 * <li>http://thepatternsite.com/fallwedge.html<br>
 * <li>https://en.wikipedia.org/wiki/Wedge_pattern<br>
 * <li>http://www.investopedia.com/university/charts/charts7.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
abstract class Wedges extends Triangles {

  private final TrendSlopes slope;

  public Wedges(final int awayPoints, final double thresholdPercentage, final TrendSlopes slope, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, slope, slope, endpointVicinity);
    if (FLAT == slope) {
      throw new IllegalArgumentException();
    }

    this.slope = slope;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    return slope.isRightDirection(trendline.m()) && // wedge
           !trendline.isPracticallyHorizontal();
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    return hasIntersectRight(candidate, counterpart) &&
           hasPotential(counterpart) &&
           super.isComplementary(candidate, counterpart);
  }

  @Override
  protected boolean hasIntersectRight(final Trendline upper, final Trendline lower) {
    return (UP == slope)   ? super.hasIntersectRight(lower, upper) :
           (DOWN == slope) ? super.hasIntersectRight(upper, lower) :
           false;
  }

}
