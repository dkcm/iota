/**
 * AroonOscillator.java  v0.2  9 December 2014 12:04:45 PM
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
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator
 * https://www.incrediblecharts.com/indicators/aroon_oscillator.php
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AroonOscillator extends AbstractIndicator {

  public AroonOscillator() {
    this(TWENTY_FIVE);
  }

  public AroonOscillator(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Aroon Oscillator = Aroon-Up - Aroon-Down

    // compute Aroons
    final double[] aroonUp = new double[ohlcv.size() - lookback];
    final double[] aroonDown = new double[aroonUp.length];

    final RetCode outcome = TA_LIB.aroon(start,
                                         end,
                                         ohlcv.highs(),
                                         ohlcv.lows(),
                                         period,
                                         outBegIdx,
                                         outNBElement,
                                         aroonDown,
                                         aroonUp);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = aroonUp[i] - aroonDown[i];
    }

    return outcome;

//    return TA_LIB.aroonOsc(start,
//                           end,
//                           ohlcv.highs(),
//                           ohlcv.lows(),
//                           period,
//                           outBegIdx,
//                           outNBElement,
//                           output);
  }

}
