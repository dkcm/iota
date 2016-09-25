/**
 * CMF.java  v0.2  3 January 2015 11:21:10 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chaikin Money Flow (CMF) by Marc Chaikin
 * <p>
 * "Chaikin Money Flow measures buying and selling pressure for a given period
 * of time. A move into positive territory indicates buying pressure, while a
 * move into negative territory indicates selling pressure. Chartists can use
 * the absolute value of Chaikin Money Flow to confirm or question the price
 * action of the underlying. Positive CMF would confirm an uptrend, but negative
 * CMF would call into question the strength behind an uptrend. The reverse
 * holds true for downtrends."
 * <p>
 * http://www.chaikinpowertools.com/education_chaikin-money-flow-technical-indicator.shtml
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf
 * http://www.incrediblecharts.com/indicators/chaikin_money_flow.php
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V18/C08/072CHA.pdf
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CMF extends AbstractIndicator {

  private static final MoneyFlowVolume MFV = new MoneyFlowVolume();

  public CMF() {
    this(TWENTY_ONE);
  }

  public CMF(final int period) {
    super(period, period - ONE);
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
    // 3. n-period CMF = n-period Sum of Money Flow Volume / n-period Sum of Volume

    // compute Money Flow Volume
    final double[] mfv = new double[ohlcv.size()];
    MFV.compute(start, end, ohlcv, outBegIdx, outNBElement, mfv);

    // compute indicator
    double smfv = ZERO;
    long sv = ZERO;
    for (int j = ZERO; j < period; ++j) {
      smfv += mfv[j];
      sv += ohlcv.volume(j);
    }
    int i = ZERO;
    output[i] = smfv / sv;
    for (int j = period, k = ZERO; ++i < output.length; ++j, ++k) {
      smfv += mfv[j] - mfv[k];
      sv += ohlcv.volume(j) - ohlcv.volume(k);

      output[i] = smfv / sv;
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
