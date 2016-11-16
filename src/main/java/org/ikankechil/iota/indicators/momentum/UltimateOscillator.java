/**
 * UltimateOscillator.java  v0.2  4 December 2014 2:28:28 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Ultimate Oscillator by Larry Williams
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ultimate_oscillator<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V03/C04/ULTI.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class UltimateOscillator extends AbstractIndicator {

  private final int           shortPeriod;
  private final int           mediumPeriod;
  private final int           longPeriod;

  private static final double UO_FACTOR = HUNDRED_PERCENT / (FOUR + TWO + ONE); // 100 / 7

  public UltimateOscillator() {
    this(SEVEN, FOURTEEN, TWENTY_EIGHT);
  }

  public UltimateOscillator(final int shortPeriod, final int mediumPeriod, final int longPeriod) {
    super(longPeriod);
    throwExceptionIfNegative(shortPeriod,
                             mediumPeriod,
                             longPeriod);
    if (!(shortPeriod < mediumPeriod && mediumPeriod < longPeriod)) {
      throw new IllegalArgumentException(String.format("Short: %s, Medium: %s, Long: %s",
                                                       shortPeriod,
                                                       mediumPeriod,
                                                       longPeriod));
    }

    this.shortPeriod = shortPeriod;
    this.mediumPeriod = mediumPeriod;
    this.longPeriod = longPeriod;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Buying Pressure, BP = Close - Minimum(Low or Prior Close).
    // True Range, TR = Maximum(High or Prior Close) - Minimum(Low or Prior Close)
    //
    // Average7 = (7-period BP Sum) / (7-period TR Sum)
    // Average14 = (14-period BP Sum) / (14-period TR Sum)
    // Average28 = (28-period BP Sum) / (28-period TR Sum)
    //
    // Ultimate Oscillator, UO = 100 x [(4 x Average7)+(2 x Average14)+Average28]/(4+2+1)

    final int size = ohlcv.size();
    final double[] bp = new double[size - ONE];
    final double[] tr = new double[bp.length];

    // compute buying pressure and true range
    int i = ZERO;
    double pclose = ohlcv.close(i);
    for (int j = ZERO; ++i < size; ++j) {
      final double close = ohlcv.close(i);
      final double min = Math.min(ohlcv.low(i), pclose);
      bp[j] = close - min;
      tr[j] = Math.max(ohlcv.high(i), pclose) - min;

      // shift forward in time
      pclose = close;
    }

    // compute averages
    final double[] averageShort = ave(shortPeriod, bp, tr);
    final double[] averageMedium = ave(mediumPeriod, bp, tr);
    final double[] averageLong = ave(longPeriod, bp, tr);

    // compute indicator
    for (int l = ZERO, m = longPeriod - mediumPeriod, s = longPeriod - shortPeriod;
         l < output.length;
         ++l, ++m, ++s) {
      output[l] = UO_FACTOR * ((FOUR * averageShort[s]) + (TWO * averageMedium[m]) + averageLong[l]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double[] ave(final int p, final double[] bp, final double[] tr) {
    final double[] bpSum = sum(p, bp);
    final double[] trSum = sum(p, tr);

    final double[] ave = new double[bp.length - p + ONE];
    for (int i = ZERO; i < ave.length; ++i) {
      ave[i] = bpSum[i] / trSum[i];
    }

    return ave;
  }

}
