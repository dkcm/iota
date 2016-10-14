/**
 * KasePeakOscillator.java  v0.1  3 August 2015 6:06:29 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.RWI;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Kase Peak Oscillator by Cynthia Kase
 *
 * <p>http://www.kaseco.com/support/articles/The_Two_Faces_of_Momentum.pdf<br>
 * http://beathespread.com/file/download/15086<br>
 * https://github.com/mtompkins/openAlgo/blob/master/MultiCharts/Indicators/Kase%20Peak%20Oscillator/<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KasePeakOscillator extends AbstractIndicator {

  private final RWI rwi;

  public KasePeakOscillator() {
    this(THIRTY);
  }

  public KasePeakOscillator(final int period) {
    super(period, period);

    rwi = new RWI(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // KPO = RWI(high) - RWI(low), where
    // RWI(high) = (High[0]-Low[n])/ATR*sqrt(n)
    // RWI(low) = (High[n]-Low[0])/ATR*sqrt(n)

    final List<TimeSeries> rwis = rwi.generate(ohlcv);
    final double[] rwiHighs = rwis.get(ZERO).values();
    final double[] rwiLows = rwis.get(ONE).values();

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = rwiHighs[i] - rwiLows[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
