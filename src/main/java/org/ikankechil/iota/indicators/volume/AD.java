/**
 * AD.java v0.2  5 December 2014 3:23:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Accumulation Distribution (AD)
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:accumulation_distribution_line
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AD extends AbstractIndicator {

  private static final MoneyFlowVolume MFV = new MoneyFlowVolume();

  public AD() {
    super(ZERO);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. Money Flow Multiplier = [(Close - Low) - (High - Close)] / (High - Low)
    // 2. Money Flow Volume = Money Flow Multiplier x Volume for the Period
    // 3. ADL = Previous ADL + Current Period's Money Flow Volume

    // compute Money Flow Volume
    final double[] mfv = new double[ohlcv.size()];
    MFV.compute(start, end, ohlcv, outBegIdx, outNBElement, mfv);

    // compute indicator
    double ad = ZERO;
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = ad += mfv[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
