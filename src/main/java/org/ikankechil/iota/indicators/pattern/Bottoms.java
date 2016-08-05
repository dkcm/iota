/**
 * Bottoms.java  v0.1  7 January 2016 9:49:53 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.Comparators.*;

import org.ikankechil.iota.indicators.Indicator;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Bottoms extends Extrema {

  public Bottoms() {
    this(FIVE);
  }

  public Bottoms(final int awayPoints) {
    this(awayPoints, null);
  }

  public Bottoms(final int awayPoints, final Indicator indicator) {
    this(awayPoints, indicator, false);
  }

  public Bottoms(final int awayPoints, final Indicator indicator, final boolean interpolate) {
    super(awayPoints, indicator, interpolate, BOTTOMS);
  }

}
