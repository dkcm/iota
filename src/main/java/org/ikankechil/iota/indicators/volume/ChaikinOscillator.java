/**
 * ChaikinOscillator.java  v0.1  4 December 2014 3:13:53 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chaikin Oscillator by Marc Chaikin
 * <p>
 * "It is designed to measure the momentum behind buying and selling pressure
 * (Accumulation Distribution Line). A move into positive territory indicates
 * that the Accumulation Distribution Line is rising and buying pressure
 * prevails. A move into negative territory indicates that the Accumulation
 * Distribution Line is falling and selling pressure prevails. Chartists can
 * anticipate crosses into positive or negative territory by looking for bullish
 * or bearish divergences, respectively."
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:
 * chaikin_oscillator
 *
 * @author Daniel Kuan
 * @version
 */
public class ChaikinOscillator extends AbstractIndicator {

  private final int fast;
  private final int slow;

  public ChaikinOscillator() {
    this(THREE, TEN);
  }

  public ChaikinOscillator(final int fast, final int slow) {
    super(TA_LIB.adOscLookback(fast, slow));
    throwExceptionIfNegative(fast, slow);

    this.fast = fast;
    this.slow = slow;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    return TA_LIB.adOsc(start,
                        end,
                        ohlcv.highs(),
                        ohlcv.lows(),
                        ohlcv.closes(),
                        toDoubles(ohlcv.volumes()), // copy volumes
                        fast,
                        slow,
                        outBegIdx,
                        outNBElement,
                        output);
  }

}
