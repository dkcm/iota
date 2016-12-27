/**
 * AscendingTriangleBreakout.java  0.1  24 December 2016 11:12:03 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.strategies.pattern;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.AscendingTriangle;
import org.ikankechil.iota.indicators.pattern.Trendlines;

/**
 * Ascending triangle breakout strategy.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AscendingTriangleBreakout extends TrendlineBreakout {

  private final Indicator ascendingTriangle;

  public AscendingTriangleBreakout(final Trendlines trendlines) {
    super(trendlines);

    this.ascendingTriangle = new AscendingTriangle(trendlines);
  }

  @Override
  protected List<List<TimeSeries>> generateIndicatorValues(final OHLCVTimeSeries ohlcv) {
    return Arrays.asList(ascendingTriangle.generate(ohlcv));
  }

}
