/**
 * RightAngledTriangles.java  0.3  23 December 2016 12:49:31 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Abstract class representing ascending and descending triangles.  It searches
 * for complementary counterpart trendlines only when a candidate trendline is
 * flat.
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:ascending_triangle_continuation<br>
 * <li>http://thepatternsite.com/at.html<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:descending_triangle_continuation<be>
 * <li>http://thepatternsite.com/dt.html<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
abstract class RightAngledTriangles extends Triangles {

  public RightAngledTriangles(final int awayPoints, final double thresholdPercentage, final TrendSlopes upper, final TrendSlopes lower, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, upper, lower, endpointVicinity);
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
