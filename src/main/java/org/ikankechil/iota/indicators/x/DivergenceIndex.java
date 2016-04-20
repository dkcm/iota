/**
 * DivergenceIndex.java	v0.1	22 November 2015 10:41:10 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.x;

import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Divergence Index by Matt Storz
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C01/QUANTIF.pdf
 * http://www.multicharts.com/support/base/?action=article&id=1758
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class DivergenceIndex extends AbstractIndicator {

  private final Indicator indicator;

  public DivergenceIndex() {
    this(null);
  }

  public DivergenceIndex(final Indicator indicator) {
    super(ZERO);

    this.indicator = indicator;
  }

  public static void main(final String[] args) {
    // TODO Auto-generated method stub

  }

  // Formula:
  // Peak divergence, Dp = 50 (((Hc - Hp)/(Hh - Hl))-((Ic - Ip)/(Ih - Il)))
  // where
  //  Hc = High at the current peak
  //  Hp = High at the previous peak
  //  Hh = Highest high from the previous to the current peak
  //  Hl = Lowest high from the previous to the current peak
  //  Ic = Indicator value at the current peak
  //  Ip = Indicator value at the previous peak
  //  Ih = Highest indicator value from the previous to the current peak
  //  Il = Lowest indicator value from the previous to the current peak
  //
  // Trough divergence, Dt = 50 (((Ic - Ip)/(Ih - Il))-((Lc - Lp)/(Lh - Ll)))
  // where
  //  Ic = Indicator value at the current trough
  //  Ip = Indicator value at the previous trough
  //  Ih = Highest indicator value from the previous to the current trough
  //  Il = Lowest indicator value from the previous to the current trough
  //  Lc = Low at the current trough
  //  Lp = Low at the previous trough
  //  Lh = Highest low from the previous to the current trough
  //  Ll = Lowest low from the previous to the current trough
  //
  // The more positive the divergence index is, the more likely a price reversal
  // will occur. Negative divergences are often associated with false peaks.

}
