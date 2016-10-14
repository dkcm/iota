/**
 * TopsAndBottoms.java  v0.4  31 December 2015 9:53:03 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.Comparators.*;

import org.ikankechil.iota.indicators.Indicator;

/**
 * <code>Tops</code> and <code>Bottoms</code> combined into one
 * <code>Indicator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class TopsAndBottoms extends Extrema {

  public TopsAndBottoms() {
    this(FIVE);
  }

  public TopsAndBottoms(final int awayPoints) {
    this(awayPoints, null);
  }

  public TopsAndBottoms(final int awayPoints, final Indicator indicator) {
    this(awayPoints, indicator, false);
  }

  public TopsAndBottoms(final int awayPoints, final Indicator indicator, final boolean interpolate) {
    super(awayPoints, indicator, interpolate, TOPS, BOTTOMS);
  }

}
