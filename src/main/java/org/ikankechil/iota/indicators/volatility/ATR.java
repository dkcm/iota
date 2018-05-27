/**
 * ATR.java  v0.4  4 December 2014 12:46:50 PM
 *
 * Copyright © 2014-2018 Daniel Kuan.  All rights reserved.
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
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_true_range_atr
 * <li>https://www.tradingview.com/wiki/Average_True_Range_(ATR)
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C03/054ATR.pdf
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V19/C06/067VOL.pdf
 * <li>https://www.fidelity.com/bin-public/060_www_fidelity_com/documents/AverageTrueRange.pdf<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class ATR extends AbstractIndicator {

  private final double inversePeriod;
  private final double oneMinusInversePeriod;

  public ATR() {
    this(FOURTEEN);
  }

  public ATR(final int period) {
    super(period, period);

    inversePeriod = ONE / (double) period;
    oneMinusInversePeriod = ONE - inversePeriod;
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

    double atr = computeInitialATR(ohlcv);

    // compute indicator
    int i = ZERO;
    int j = period;
    double close = ohlcv.close(j);
    output[i] = normalise(atr, close);
    while (++i < output.length) {
      final double tr = trueRange(ohlcv.high(++j), ohlcv.low(j), close);
      atr = computeATR(atr, tr);
      close = ohlcv.close(j);
      output[i] = normalise(atr, close);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  /**
   *
   *
   * @param atr
   * @param close
   * @return
   */
  double normalise(final double atr, final double close) {
    return atr;
  }

  private double computeInitialATR(final OHLCVTimeSeries ohlcv) {
    // initial ATR = simple average
    double sum = ZERO;
    int i = ZERO;
    while (i < period) {
      final double close = ohlcv.close(i);
      sum += trueRange(ohlcv.high(++i), ohlcv.low(i), close);
    }
    return sum * inversePeriod;
  }

  private double computeATR(final double atr, final double tr) {
    return (oneMinusInversePeriod * atr) + (inversePeriod * tr);
  }

}
