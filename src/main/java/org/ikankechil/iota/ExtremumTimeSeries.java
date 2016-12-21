/**
 * ExtremumTimeSeries.java  0.1  21 December 2016 6:51:30 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota;

import java.util.ArrayList;
import java.util.List;

import org.ikankechil.iota.indicators.pattern.Extrema.Extremum;

/**
 * A time-series representation of extrema.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ExtremumTimeSeries extends TimeSeries {

  private final List<? extends Extremum> extrema;

  public ExtremumTimeSeries(final String name, final String[] dates, final double[] extremes, final List<? extends Extremum> extrema) {
    super(name, dates, extremes);
    if (extrema == null) {
      throw new NullPointerException("Extrema cannot be null");
    }

    this.extrema = extrema;
  }

  public List<Extremum> extrema() {
    return new ArrayList<>(extrema); // copy
  }

}
