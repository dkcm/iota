/**
 * Tops.java  v0.1  7 January 2016 9:49:36 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.Comparators.*;

import org.ikankechil.iota.indicators.Indicator;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Tops extends Extrema {

  public Tops() {
    this(FIVE);
  }

  public Tops(final int awayPoints) {
    this(awayPoints, null);
  }

  public Tops(final int awayPoints, final Indicator indicator) {
    this(awayPoints, indicator, false);
  }

  public Tops(final int awayPoints, final Indicator indicator, final boolean interpolate) {
    super(awayPoints, indicator, interpolate, TOPS);
  }

}
