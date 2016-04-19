/**
 * ATR.java v0.1  4 December 2014 12:46:50 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average True Range (ATR) by J. Welles Wilder
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C03/054ATR.pdf
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C06/067VOL.pdf
 *
 * @author Daniel Kuan
 * @version
 */
public class ATR extends AbstractIndicator {

  public ATR() {
    this(FOURTEEN);
  }

  public ATR(final int period) {
    super(period, TA_LIB.atrLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.atr(start,
                      end,
                      ohlcv.highs(),
                      ohlcv.lows(),
                      ohlcv.closes(),
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
