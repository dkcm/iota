/**
 * AVO.java  v0.1  15 December 2016 6:37:04 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.trend.APO;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Absolute Volume Oscillator (AVO)
 *
 * <p>http://www.stockfetcher.com/forums2/Indicators/Absolute-Volume-Oscillator-AVO/27498<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AVO extends APO {

  public AVO() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public AVO(final int fast, final int slow, final int signal) {
    super(fast, slow, signal);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return compute(start,
                   end,
                   toDoubles(ohlcv.volumes()),
                   outBegIdx,
                   outNBElement,
                   output);
  }

}
