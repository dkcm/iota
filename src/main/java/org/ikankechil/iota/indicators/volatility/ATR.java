/**
 * ATR.java  v0.3  4 December 2014 12:46:50 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import static org.ikankechil.iota.indicators.volatility.TR.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average True Range (ATR) by J. Welles Wilder
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C03/054ATR.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C06/067VOL.pdf<br>
 * https://www.fidelity.com/bin-public/060_www_fidelity_com/documents/AverageTrueRange.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class ATR extends AbstractIndicator {

  private final int    period_1;
  private final double inversePeriod;

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

    // compute initial ATR
    double atr = ZERO;
    int j = ZERO;
    double close = ohlcv.close(j);
    while (j < period) {
      atr += trueRange(ohlcv.high(++j), ohlcv.low(j), close);
      close = ohlcv.close(j);
    }
    atr *= inversePeriod;

    // compute indicator
    int i = ZERO;
    output[i] = normalise(atr, close);
    while (++i < output.length) {
      final double tr = trueRange(ohlcv.high(++j), ohlcv.low(j), close);
      atr = ((period_1 * atr) + tr) * inversePeriod;
      close = ohlcv.close(j);
      output[i] = normalise(atr, close);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  /**
   * @param close
   */
  double normalise(final double atr, final double close) {
    return atr;
  }

}
