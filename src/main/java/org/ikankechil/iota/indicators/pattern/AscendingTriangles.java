/**
 * AscendingTriangles.java  0.2  22 December 2016 7:07:09 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Ascending Triangles
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:ascending_triangle_continuation<br>
 * http://www.investopedia.com/terms/a/ascendingtriangle.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AscendingTriangles extends RightAngledTriangles {

  public AscendingTriangles(final Trendlines trendlines) {
    super(trendlines);
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
    return counterpart;
  }

}
