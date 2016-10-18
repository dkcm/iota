/**
 * DEMA.java  v0.2  9 December 2014 12:10:01 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Double Exponential Moving Average (DEMA)by Patrick Mulloy
 *
 * <p>www.investopedia.com/articles/trading/10/double-exponential-moving-average.asp<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V12/C01/SMOOTHI.pdf<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DEMA extends AbstractIndicator {

  public DEMA() {
    this(THIRTY);
  }

  public DEMA(final int period) {
    super(period, TA_LIB.demaLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.dema(start,
                       end,
                       values,
                       period,
                       outBegIdx,
                       outNBElement,
                       output);
  }

}
