/**
 * DescendingTriangle.java  0.1  23 December 2016 2:58:28 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Descending Triangle
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:descending_triangle_continuation<be>
 * http://www.investopedia.com/terms/d/descendingtriangle.asp<be>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DescendingTriangle extends RightAngledTriangle {

  public DescendingTriangle(final Trendlines trendlines) {
    super(trendlines);
  }

  @Override
  List<Trendline> selectPotentiallyFlatTrendlines(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return upTrendlines;
  }

  @Override
  List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return downTrendlines;
  }

  @Override
  Trendline selectTop(final Trendline trendline, final Trendline counterpart) {
    return counterpart;
  }

  @Override
  Trendline selectBottom(final Trendline trendline, final Trendline counterpart) {
    return trendline;
  }

}
