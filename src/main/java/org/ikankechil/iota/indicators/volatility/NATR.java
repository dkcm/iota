/**
 * NATR.java  v0.2  15 December 2014 11:57:02 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Normalised Average True Range (NATR) by John Forman
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V24/C05/094FOR.pdf
 * http://traders.com/Documentation/FEEDbk_docs/2006/05/TradersTips/TradersTips.html
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class NATR extends AbstractIndicator {

  private final ATR atr;

  public NATR() {
    this(FOURTEEN);
  }

  public NATR(final int period) {
    super(period, period);

    atr = new ATR(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // NATR = atr / Close * 100

    final TimeSeries atrs = atr.generate(ohlcv).get(ZERO);

    // compute indicator
    for (int i = ZERO, j = lookback; i < output.length; ++i, ++j) {
      output[i] = atrs.value(i) / ohlcv.close(j) * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
