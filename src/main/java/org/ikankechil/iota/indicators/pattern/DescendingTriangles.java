/**
 * DescendingTriangles.java  0.2  23 December 2016 2:58:28 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Descending Triangles
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:descending_triangle_continuation<be>
 * http://www.investopedia.com/terms/d/descendingtriangle.asp<be>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DescendingTriangles extends RightAngledTriangles {

  public DescendingTriangles(final Trendlines trendlines) {
    super(trendlines);
  }

  @Override
  protected List<Trendline> selectCandidates(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return upTrendlines;
  }

  @Override
  protected List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return downTrendlines;
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
