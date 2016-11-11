/**
 * ADX.java  v0.2  4 December 2014 1:13:05 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average Directional Movement Index (ADX) by J. Welles Wilder
 *
 * <p>"The ADX identifies whether the stock or commodity is trending. However,
 * it does not reveal the direction the stock is trending, only that it is
 * trending: The stronger the trend, the higher the ADX value."
 *
 * <p>http://www.investopedia.com/articles/trading/07/adx-trend-indicator.asp<br>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx<br>
 * https://www.linnsoft.com/techind/adx-avg-directional-movement<br>
 * http://www.futuresmag.com/2014/09/30/measuring-trend-strength-and-sensitivity<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ADX extends AbstractIndicator {

  private final int    period_1;
  private final double inversePeriod;
  private final DX     dx;

  public ADX() {
    this(FOURTEEN);
  }

  public ADX(final int period) {
    super(period, (period << ONE) - ONE);

    period_1 = period - ONE;
    inversePeriod = ONE / (double) period;
    dx = new DX(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Calculate the True Range (TR), Plus Directional Movement (+DM) and
    //    Minus Directional Movement (-DM) for each period
    // 2. Smooth these periodic values using the Wilder's smoothing techniques.
    // 3. Divide the 14-day smoothed Plus Directional Movement (+DM) by the 14-
    //    day smoothed True Range to find the 14-day Plus Directional Indicator
    //    (+DI14). Multiply by 100 to move the decimal point two places.
    // 4. Divide the 14-day smoothed Minus Directional Movement (-DM) by the 14-
    //    day smoothed True Range to find the 14-day Minus Directional Indicator
    //    (-DI14). Multiply by 100 to move the decimal point two places.
    // 5. The Directional Movement Index (DX) equals the absolute value of +DI14
    //    less -DI14 divided by the sum of +DI14 and -DI14. Multiply the result
    //    by 100 to move the decimal point over two places.
    // 6. After all these steps, it is time to calculate the Average Directional
    //    Index (ADX). The first ADX value is simply a 14-day average of DX.
    //    Subsequent ADX values are smoothed by multiplying the previous 14-day
    //    ADX value by 13, adding the most recent DX value and dividing this
    //    total by 14.

    // compute DX
    final double[] dxs = dx.generate(ohlcv).get(ZERO).values();

    // compute initial ADX
    int j = ZERO;
    double adx = dxs[j];
    while (++j < period) {
      adx += dxs[j];
    }
    adx *= inversePeriod;

    // compute indicator
    int i = ZERO;
    output[i] = adx;
    while (++i < output.length) {
      output[i] = adx = ((period_1 * adx) + dxs[j++]) * inversePeriod;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
