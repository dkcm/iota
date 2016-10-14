/**
 * DeMarker.java  v0.2  15 December 2014 12:28:19 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * DeMarker Indicator by Tom DeMark
 *
 * <p>http://ta.mql4.com/indicators/oscillators/DeMarker<br>
 * http://www.investopedia.com/ask/answers/120514/what-demarker-indicator-formula-and-how-it-calculated.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DeMarker extends AbstractIndicator {

  public DeMarker() {
    this(FOURTEEN);
  }

  public DeMarker(final int period) {
    super(period, TA_LIB.smaLookback(period) + ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Calculation:
    // The value of the DeMarker for the "i" interval is calculated as follows:
    // The DeMax(i) is calculated:
    // If high(i) > high(i-1) , then DeMax(i) = high(i)-high(i-1), otherwise DeMax(i) = 0
    //
    // The DeMin(i) is calculated:
    // If low(i) < low(i-1), then DeMin(i) = low(i-1)-low(i), otherwise DeMin(i) = 0
    //
    // The value of the DeMarker is calculated as:
    // DMark(N) = SMA(DeMax, N)/(SMA(DeMax, N)+SMA(DeMin, N))

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    // compute DeMax(i) and DeMin(i)
    final int size = (end - start);
    final double[] deMax = new double[size];
    final double[] deMin = new double[size];

    int j = ZERO;
    double previousHigh = highs[j];
    double previousLow = lows[j];
    for (int i = ZERO; i < end; ++i) {
      final double currentHigh = highs[++j];
      final double currentLow = lows[j];
      final double deltaHigh = currentHigh - previousHigh;
      if (deltaHigh > ZERO) {
        deMax[i] = deltaHigh;
      }
      final double deltaLow = currentLow - previousLow;
      if (deltaLow < ZERO) {
        deMin[i] = -deltaLow;
      }
//      // alternate
//      if (currentHigh > previousHigh) {
//        deMax[i] = currentHigh - previousHigh;
//      }
//      if (currentLow < previousLow) {
//        deMin[i] = previousLow - currentLow;
//      }
      previousHigh = currentHigh;
      previousLow = currentLow;
    }

    // compute SMA DeMax
    final double[] smaDeMax = sma(deMax, period);

    // compute SMA DeMin
    final double[] smaDeMin = sma(deMin, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = smaDeMax[i] / (smaDeMax[i] + smaDeMin[i]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
