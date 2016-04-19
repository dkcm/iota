/**
 * AroonOscillator.java v0.1 9 December 2014 12:04:45 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Aroon Oscillator
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AroonOscillator extends AbstractIndicator {

  public AroonOscillator() {
    this(FOURTEEN);
  }

  public AroonOscillator(final int period) {
    super(period, TA_LIB.aroonOscLookback(period));
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.aroonOsc(start,
                           end,
                           ohlcv.highs(),
                           ohlcv.lows(),
                           period,
                           outBegIdx,
                           outNBElement,
                           output);
  }

}
