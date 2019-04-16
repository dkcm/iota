/**
 * ForecastOscillator.java  v0.1  14 April 2019 8:16:35 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.trend.TimeSeriesForecast;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Forecast Oscillator by Tushar Chande
 *
 * <p>References:
 * <li>https://www.fmlabs.com/reference/default.htm?url=ForecastOscillator.htm
 * <li>https://library.tradingtechnologies.com/trade/chrt-ti-chande-forecast-oscillator.html
 * <li>https://user42.tuxfamily.org/chart/manual/Forecast-Oscillator.html
 * <li>https://www.quantshare.com/item-926-forecast-oscillator<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ForecastOscillator extends AbstractIndicator {

  private final Indicator tsf;

  public ForecastOscillator() {
    this(FOURTEEN);
  }

  public ForecastOscillator(final int period) {
    super(period, period - ONE);

    tsf = new TimeSeriesForecast(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // FO = (close - TSF) / close x 100

    final double[] forecast = tsf.generate(new TimeSeries(EMPTY, new String[values.length], values), start).get(ZERO).values();

    // compute indicator
    for (int i = start, v = start + lookback; i < output.length; ++i, ++v) {
      final double close = values[v];
      output[i] = (close - forecast[i]) / close * HUNDRED_PERCENT;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
