/**
 * ATR.java  v0.2  4 December 2014 12:46:50 PM
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
 * Average True Range (ATR) by J. Welles Wilder
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C03/054ATR.pdf
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C06/067VOL.pdf
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ATR extends AbstractIndicator {

  private final int       period_1;
  private final double    inversePeriod;

  private static final TR TR = new TR();

  public ATR() {
    this(FOURTEEN);
  }

  public ATR(final int period) {
    super(period, period);

    period_1 = period - ONE;
    inversePeriod = ONE / (double) period;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // ATR = ((13 x ATRp) + TR) / 14

    final TimeSeries tr = TR.generate(ohlcv).get(ZERO);

    // compute initial ATR
    double atr = ZERO;
    for (int j = ZERO; j < period; ++j) {
      atr += tr.value(j);
    }
    atr *= inversePeriod;

    // compute indicator
    int i = ZERO;
    output[i] = atr;
    for (int j = period; ++i < output.length; ++j) {
      atr = output[i] = ((period_1 * atr) + tr.value(j)) * inversePeriod;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
