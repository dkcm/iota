/**
 * ROCV.java  v0.1  10 April 2018 11:49:22 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.momentum.ROC;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volume Rate of Change (ROCV)
 *
 * <p>References:
 * <li>http://www.ta-guru.com/Book/TechnicalAnalysis/TechnicalIndicators/VolumeRateOfChange.php5<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ROCV extends AbstractIndicator {

  private final Indicator roc;

  public ROCV() {
    this(TWELVE);
  }

  public ROCV(final int period) {
    super(period, period);

    roc = new ROC(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // ROCV = [(Volume - Volume n periods ago) / (Volume n periods ago)] * 100

    // compute indicator
    final TimeSeries volume = new TimeSeries(EMPTY, ohlcv.dates(), toDoubles(ohlcv.volumes()));
    final TimeSeries rocv = roc.generate(volume).get(ZERO);
    System.arraycopy(rocv.values(), ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}