/**
 * DescendingTriangleBreakout.java  0.1  24 December 2016 11:28:57 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.DescendingTriangles;
import org.ikankechil.iota.indicators.pattern.Trendlines;

/**
 * Descending triangle breakout strategy.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DescendingTriangleBreakout extends TrendlineBreakout {

  private final Indicator descendingTriangles;

  public DescendingTriangleBreakout(final Trendlines trendlines) {
    super(trendlines);

    descendingTriangles = new DescendingTriangles(trendlines);
  }

  @Override
  protected List<List<TimeSeries>> generateIndicatorValues(final OHLCVTimeSeries ohlcv) {
    return Arrays.asList(descendingTriangles.generate(ohlcv));
  }

}
