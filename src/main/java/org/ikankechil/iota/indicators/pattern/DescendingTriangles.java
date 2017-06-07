/**
 * DescendingTriangles.java  0.3  23 December 2016 2:58:28 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Descending Triangles
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:descending_triangle_continuation<be>
 * <li>http://thepatternsite.com/dt.html<br>
 * <li>http://www.investopedia.com/terms/d/descendingtriangle.asp<be>
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class DescendingTriangles extends RightAngledTriangles {

  public DescendingTriangles(final int awayPoints, final double thresholdPercentage) {
    this(awayPoints, thresholdPercentage, ENDPOINT_VICINITY);
  }

  public DescendingTriangles(final int awayPoints, final double thresholdPercentage, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, DOWN, FLAT, endpointVicinity);
  }

  @Override
  protected List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    // select the flat side
    return lowerTrendlineTimeSeries.trendlines();
  }

  @Override
  protected List<Trendline> selectCounterparts(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    return upperTrendlineTimeSeries.trendlines();
  }

  @Override
  protected Trendline selectTop(final Trendline candidate, final Trendline counterpart) {
    return counterpart;
  }

  @Override
  protected Trendline selectBottom(final Trendline candidate, final Trendline counterpart) {
    return candidate;
  }

}
