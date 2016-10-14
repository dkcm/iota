/**
 * KaseCD.java  v0.1  6 November 2015 10:05:56 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.momentum.KasePeakOscillator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * KaseCD by Cynthia Kase
 *
 * <p>http://www.kaseco.com/support/articles/The_Two_Faces_of_Momentum.pdf<br>
 * http://www.kasestatware.com/about/kasecd.htm<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
class KaseCD extends AbstractIndicator {

  private final KasePeakOscillator peakOscillator;
  private final int                signal;

  public KaseCD() {
    this(THIRTY, NINE);
  }

  public KaseCD(final int period, final int signal) {
    super(period, signal - ONE);
    throwExceptionIfNegative(signal);

    peakOscillator = new KasePeakOscillator(period);
    this.signal = signal;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // KCD = Kase Peak Oscillator minus its average
    final double[] kpo = peakOscillator.generate(ohlcv).get(ZERO).values();
    final double[] kpoEMA = ema(kpo, signal);

    // compute indicator
    for (int i = ZERO, j = lookback; i < output.length; ++i, ++j) {
      output[i] = kpo[j] - kpoEMA[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
