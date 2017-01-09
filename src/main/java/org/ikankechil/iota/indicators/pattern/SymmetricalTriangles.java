/**
 * SymmetricalTriangles.java  v0.1  5 January 2017 10:08:06 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Symmetrical Triangles
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:symmetrical_triangle_continuation<br>
 * http://www.investopedia.com/university/charts/charts5.asp<br>
 * http://www.investopedia.com/terms/s/symmetricaltriangle.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SymmetricalTriangles extends Triangles {

  /**
   *
   *
   * @param trendlines
   */
  public SymmetricalTriangles(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin number of OHLCV bars the heads and tails of the
   *          trendlines forming the triangle must be in the vicinity of
   */
  public SymmetricalTriangles(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
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
    return counterpart;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    final double gradient = trendline.m();
    return (gradient < ZERO && gradient > Double.NEGATIVE_INFINITY) &&
           !trendline.isPracticallyHorizontal();
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    final double gradient = counterpart.m();
    return (gradient > ZERO && gradient < Double.POSITIVE_INFINITY) &&
           !counterpart.isPracticallyHorizontal() &&
           super.isComplementary(candidate, counterpart);
  }

}
