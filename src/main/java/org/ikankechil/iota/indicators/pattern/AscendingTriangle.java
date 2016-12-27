/**
 * AscendingTriangle.java  0.1  22 December 2016 7:07:09 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Ascending Triangle
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:ascending_triangle_continuation<br>
 * http://www.investopedia.com/terms/a/ascendingtriangle.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AscendingTriangle extends RightAngledTriangle {

  public AscendingTriangle(final Trendlines trendlines) {
    super(trendlines);
  }

  @Override
  List<Trendline> selectPotentiallyFlatTrendlines(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return downTrendlines;
  }

  @Override
  List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines) {
    return upTrendlines;
  }

  @Override
  Trendline selectTop(final Trendline trendline, final Trendline counterpart) {
    return trendline;
  }

  @Override
  Trendline selectBottom(final Trendline trendline, final Trendline counterpart) {
    return counterpart;
  }

}
