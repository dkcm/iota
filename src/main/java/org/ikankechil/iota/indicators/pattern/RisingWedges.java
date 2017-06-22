/**
 * RisingWedges.java  v0.3  30 December 2016 1:58:30 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;

import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Rising Wedges
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:rising_wedge_reversal<br>
 * <li>http://thepatternsite.com/risewedge.html<br>
 * <li>https://en.wikipedia.org/wiki/Wedge_pattern<br>
 * <li>http://www.investopedia.com/university/charts/charts7.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class RisingWedges extends Wedges {

  public RisingWedges(final int awayPoints, final double thresholdPercentage) {
    this(awayPoints, thresholdPercentage, ENDPOINT_VICINITY);
  }

  public RisingWedges(final int awayPoints, final double thresholdPercentage, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, UP, endpointVicinity);
  }

  @Override
  protected List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
    // select the steeper side
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
