/**
 * TrendlineTimeSeries.java  0.1  17 December 2016 12:01:15 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import java.util.ArrayList;
import java.util.List;

import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * A time-series representation of trendlines.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendlineTimeSeries extends TimeSeries {

  private final List<? extends Trendline> trendlines;

//  public TrendlineTimeSeries(final String name, final int size) {
//    this(name, new String[size], new double[size], new ArrayList<>(size));
//  }

  public TrendlineTimeSeries(final String name, final String[] dates, final double[] trends, final List<? extends Trendline> trendlines) {
    super(name, dates, trends);
    if (trendlines == null) {
      throw new NullPointerException("Trendlines cannot be null");
    }

    this.trendlines = trendlines;
  }

  public List<Trendline> trendlines() {
    return new ArrayList<>(trendlines); // copy
  }

}
