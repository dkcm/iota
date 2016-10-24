/**
 * AroonOscillator.java  v0.3  9 December 2014 12:04:45 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Aroon Oscillator
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator<br>
 * https://www.incrediblecharts.com/indicators/aroon_oscillator.php<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class AroonOscillator extends AbstractIndicator {

  private final Aroon aroon;

  public AroonOscillator() {
    this(TWENTY_FIVE);
  }

  public AroonOscillator(final int period) {
    super(period, period);

    aroon = new Aroon(period);
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
    final List<TimeSeries> aroons = aroon.generate(ohlcv);
    final TimeSeries aroonUp = aroons.get(ZERO);
    final TimeSeries aroonDown = aroons.get(ONE);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = aroonUp.value(i) - aroonDown.value(i);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
