/**
 * HMA.java  v0.1  10 October 2016 10:56:28 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Hull Moving Average (HMA) by Alan Hull
 * <p>
 * http://www.alanhull.com/hull-moving-average<br>
 * http://www.technicalindicators.net/indicators-technical-analysis/143-hma-hull-moving-average<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HMA extends AbstractIndicator {

  private final WMA wmaP;
  private final WMA wmaHalfP;
  private final WMA wmaSqrtP;

  public HMA() {
    this(SIXTEEN);
  }

  public HMA(final int period) {
    super(period, period + (int) Math.sqrt(period) - TWO);

    wmaP = new WMA(period);
    wmaHalfP = new WMA(period >> ONE);  // period / 2
    wmaSqrtP = new WMA((int) Math.sqrt(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Integer(SquareRoot(Period)) WMA [2 x Integer(Period/2) WMA(Price) - Period WMA(Price)]

    final TimeSeries wp = wmaP.generate(ohlcv).get(ZERO);
    final TimeSeries whp = wmaHalfP.generate(ohlcv).get(ZERO);

    // compute indicator
    for (int i = ZERO, j = i + (period >> ONE); i < wp.size(); ++i, ++j) {
      wp.value((TWO * whp.value(j)) - wp.value(i), i);
    }

    final double[] wsp = wmaSqrtP.generate(wp).get(ZERO).values();
    System.arraycopy(wsp, ZERO, output, ZERO, wsp.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
