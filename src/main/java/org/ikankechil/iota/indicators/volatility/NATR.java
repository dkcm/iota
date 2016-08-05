/**
 * NATR.java  v0.1  15 December 2014 11:57:02 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Normalised Average True Range (NATR)
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NATR extends AbstractIndicator {

  public NATR() {
    this(FOURTEEN);
  }

  public NATR(final int period) {
    super(period, TA_LIB.natrLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.natr(start,
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
