/**
 * TimeSeriesForecast.java  v0.1  8 December 2014 6:25:21 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Time Series Forecast (TSF)
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TimeSeriesForecast extends AbstractIndicator {

  public TimeSeriesForecast(final int period) {
    super(period, TA_LIB.tsfLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.tsf(start,
                      end,
                      values,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
