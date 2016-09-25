/**
 * ChaikinOscillator.java  v0.2  4 December 2014 3:13:53 PM
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
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator
 * https://www.tradingview.com/stock-charts-support/index.php/Chaikin_Oscillator
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ChaikinOscillator extends AbstractIndicator {

  private final int       fast;
  private final int       slow;

  private static final AD AD = new AD();  // Accumulation Distribution Line

  public ChaikinOscillator() {
    this(THREE, TEN);
  }

  public ChaikinOscillator(final int fast, final int slow) {
    super(Math.max(fast, slow) - ONE);
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
    // Formula:
    // 1. Money Flow Multiplier = [(Close  -  Low) - (High - Close)] /(High - Low)
    // 2. Money Flow Volume = Money Flow Multiplier x Volume for the Period
    // 3. ADL = Previous ADL + Current Period's Money Flow Volume
    // 4. Chaikin Oscillator = (3-day EMA of ADL) - (10-day EMA of ADL)

    // compute Accumulation Distribution Line
    final double[] ad = new double[ohlcv.size()];
    AD.compute(start, end, ohlcv, outBegIdx, outNBElement, ad);

    // compute EMAs
    final double[] emaFast = ema(ad, fast);
    final double[] emaSlow = ema(ad, slow);

    // compute indicator
    for (int i = ZERO, j = slow - fast; i < output.length; ++i, ++j) {
      output[i] = emaFast[j] - emaSlow[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;

//    return TA_LIB.adOsc(start,
//                        end,
//                        ohlcv.highs(),
//                        ohlcv.lows(),
//                        ohlcv.closes(),
//                        toDoubles(ohlcv.volumes()), // copy volumes
//                        fast,
//                        slow,
//                        outBegIdx,
//                        outNBElement,
//                        output);
  }

}
