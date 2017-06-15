/**
 * Channels.java  v0.1  6 June 2017 12:17:00 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Channels
 *
 * <p>References:
 * <li>http://thepatternsite.com/channels.html<br>
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:price_channel_continuation
 * <li>http://www.investopedia.com/terms/p/price-channel.asp
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Channels extends SimpleBoundedPattern {

  private final TrendSlopes slope;

  public Channels(final int awayPoints, final double thresholdPercentage, final TrendSlopes slope) {
    this(awayPoints, thresholdPercentage, slope, ENDPOINT_VICINITY);
  }

  public Channels(final int awayPoints, final double thresholdPercentage, final TrendSlopes slope, final int endpointVicinity) {
    super(awayPoints, thresholdPercentage, slope, slope, endpointVicinity);

    this.slope = slope;
  }

  @Override
  protected List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries) {
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

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    return slope.isRightDirection(trendline.m());
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    return isParallel(candidate, counterpart) &&
           super.isComplementary(candidate, counterpart);
  }

}
