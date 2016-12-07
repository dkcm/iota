/**
 * DPO.java  v0.1  7 December 2016 9:36:31 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Detrended Price Oscillator (DPO)
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:detrended_price_osci<br>
 * http://www.investopedia.com/terms/d/detrended-price-oscillator-dpo.asp<br>
 * https://www.tradingview.com/wiki/Detrended_Price_Oscillator_(DPO)<br>
 * https://en.wikipedia.org/wiki/Detrended_price_oscillator<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DPO extends AbstractIndicator {

  private final int offset;

  public DPO() {
    this(TWENTY);
  }

  public DPO(final int period) {
    super(period, period - ONE);

    offset = (period >> ONE) - ONE;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Price (n / 2 + 1) periods ago less the n-period simple moving average

    // compute SMA
    final double[] sma = sma(values, period);

    // compute indicator
    for (int i = ZERO, v = i + offset; i < sma.length; ++i, ++v) {
      output[i] = values[v] - sma[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
