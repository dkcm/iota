/**
 * FallingWedges.java  v0.4  30 December 2016 1:30:51 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Falling Wedges
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:falling_wedge_reversal<br>
 * <li>http://thepatternsite.com/fallwedge.html<br>
 * <li>https://en.wikipedia.org/wiki/Wedge_pattern<br>
 * <li>http://www.investopedia.com/university/charts/charts7.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class FallingWedges extends Wedges {

  public FallingWedges(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, ENDPOINT_VICINITY);
  }

  public FallingWedges(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage, final int endpointVicinity) {
    super(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, DOWN, endpointVicinity);
  }

  @Override
  protected List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    // select the steeper side
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

}
