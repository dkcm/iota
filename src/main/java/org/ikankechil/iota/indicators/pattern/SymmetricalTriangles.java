/**
 * SymmetricalTriangles.java  v0.3  5 January 2017 10:08:06 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Symmetrical Triangles
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:symmetrical_triangle_continuation<br>
 * <li>http://thepatternsite.com/st.html<br>
 * <li>http://www.investopedia.com/university/charts/charts5.asp<br>
 * <li>http://www.investopedia.com/terms/s/symmetricaltriangle.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class SymmetricalTriangles extends Triangles {

  public SymmetricalTriangles(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, ENDPOINT_VICINITY);
  }

  public SymmetricalTriangles(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage, final int endpointVicinity) {
    super(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, DOWN, UP, endpointVicinity);
  }

  @Override
  protected List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    return upperTrendlineTimeSeries.trendlines();
  }

  @Override
  protected List<Trendline> selectCounterparts(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    return lowerTrendlineTimeSeries.trendlines();
  }

  @Override
  protected Trendline selectTop(final Trendline candidate, final Trendline counterpart) {
    return candidate;
  }

  @Override
  protected Trendline selectBottom(final Trendline candidate, final Trendline counterpart) {
    return counterpart;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    return DOWN.isRightDirection(trendline.m()) &&
           !trendline.isPracticallyHorizontal();
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    return UP.isRightDirection(counterpart.m()) &&
           !counterpart.isPracticallyHorizontal() &&
           super.isComplementary(candidate, counterpart);
  }

}
