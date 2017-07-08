/**
 * ZeroLagTEMA.java  v0.1  27 September 2016 7:38:26 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Zero-Lag Triple Exponential Moving Average (ZL-TEMA)
 *
 * <p>References:
 * <li>http://forum.actfx.com/Topic4156.aspx<br>
 * <li>http://fxcodebase.com/code/viewtopic.php?f=38&t=61383<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagTEMA extends AbstractIndicator {

  private final TEMA tema;

  public ZeroLagTEMA() {
    this(TEN);
  }

  public ZeroLagTEMA(final int period) {
    this(new TEMA(period));
  }

  public ZeroLagTEMA(final TEMA tema) {
    super(tema.lookback() * TWO);

    this.tema = tema;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Function ZL_TEMA(TEMA1 As BarArray, Length, OffSet) As BarArray
    //   Dim TEMA2 As BarArray
    //   Dim Diff
    //   TEMA2 = TEMA(TEMA1, Length, OffSet)
    //   Diff = TEMA1 - TEMA2
    //   ZL_TEMA = MA_TEMA1 + Diff
    // End Function
    //
    // This indicator is based on method of data smoothing developed by Patrick
    // Mulloy (February 1994, Stocks & Commodities) and John Ehlers (March
    // 2000), allowing to avoid the lags in moving averages.
    //
    // TMA1:= TEMA(CLOSE, period);
    // TMA2:= TEMA(TMA1, period);
    // ZeroLag TEMA:= TMA1 + (TMA1 - TMA2);


    final TimeSeries tema1 = tema.generate(ohlcv).get(ZERO);
    final double[] tema2 = tema.generate(tema1).get(ZERO).values();

    for (int i = ZERO, j = i + tema.lookback(); i < output.length; ++i, ++j) {
      output[i] = (tema1.value(j) * TWO) - tema2[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
