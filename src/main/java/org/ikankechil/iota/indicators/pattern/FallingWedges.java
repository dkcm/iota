/**
 * FallingWedges.java  v0.1  30 December 2016 1:30:51 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Falling Wedges
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:falling_wedge_reversal<br>
 * http://thepatternsite.com/fallwedge.html<br>
 * https://en.wikipedia.org/wiki/Wedge_pattern<br>
 * http://www.investopedia.com/university/charts/charts7.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class FallingWedges extends Triangles {

  /**
   *
   *
   * @param trendlines
   */
  public FallingWedges(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin
   */
  public FallingWedges(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
    super(trendlines, ohlcvBarsErrorMargin);
  }

  @Override
  protected List<Trendline> selectCandidates(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return downTrendlines;
  }

  @Override
  protected List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return upTrendlines;
  }

  @Override
  protected Trendline selectTop(final Trendline candidate, final Trendline counterpart) {
    return candidate;
  }

  @Override
  protected Trendline selectBottom(final Trendline candidate, final Trendline counterpart) {
    return null;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    final double gradient = trendline.m();
    return (gradient < ZERO && gradient > Double.NEGATIVE_INFINITY) && // falling wedge
           !trendline.isPracticallyHorizontal();
  }

}
