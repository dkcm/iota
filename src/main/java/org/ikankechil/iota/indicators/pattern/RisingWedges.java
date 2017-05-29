/**
 * RisingWedges.java  v0.1  30 December 2016 1:58:30 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Rising Wedges
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:rising_wedge_reversal<br>
 * http://thepatternsite.com/risewedge.html<br>
 * https://en.wikipedia.org/wiki/Wedge_pattern<br>
 * http://www.investopedia.com/university/charts/charts7.asp<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RisingWedges extends Triangles {

  /**
   *
   *
   * @param trendlines
   */
  public RisingWedges(final Trendlines trendlines) {
    super(trendlines);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin
   */
  public RisingWedges(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
    super(trendlines, ohlcvBarsErrorMargin);
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Trendline selectBottom(final Trendline candidate, final Trendline counterpart) {
    return candidate;
  }

  @Override
  protected boolean hasPotential(final Trendline trendline) {
    final double gradient = trendline.m();
    return (gradient > ZERO && gradient < Double.POSITIVE_INFINITY) && // rising wedge
           !trendline.isPracticallyHorizontal();
  }

  @Override
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    final double mt = candidate.m();
    final double mb = counterpart.m();
    return (mt < mb) && hasPotential(counterpart);
  }

}
