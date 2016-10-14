/**
 * MaddoxMomentum.java  v0.1  24 July 2015 12:21:58 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import com.tictactec.ta.lib.MAType;

/**
 * Maddox Momentum by Darryl W. Maddox
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V09/C04/CALCULA.pdf<br>
 * http://exceltechnical.web.fc2.com/mm.html<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MaddoxMomentum extends ForceIndex {

  public MaddoxMomentum() {
    this(TEN);
  }

  public MaddoxMomentum(final int period) {
    super(period, MAType.Sma);

    // Formula:
    // Maddox Momentum =
    // a 10-day moving average of the product of the daily change in the closing
    // price and daily volume
  }

}
