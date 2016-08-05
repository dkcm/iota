/**
 * ADX.java  v0.1  4 December 2014 1:13:05 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Average Directional Movement Index (ADX) by George Lane
 * <p>
 * "The ADX identifies whether the stock or commodity is trending. However, it
 * does not reveal the direction the stock is trending, only that it is
 * trending: The stronger the trend, the higher the ADX value."
 *
 * http://www.investopedia.com/articles/trading/07/adx-trend-indicator.asp
 * http://www.futuresmag.com/2014/09/30/measuring-trend-strength-and-sensitivity
 *
 * @author Daniel Kuan
 * @version
 */
public class ADX extends AbstractIndicator {

  public ADX() {
    this(FOURTEEN);
  }

  public ADX(final int period) {
    super(period, TA_LIB.adxLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.adx(start,
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
