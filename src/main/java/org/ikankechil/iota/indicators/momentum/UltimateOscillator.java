/**
 * UltimateOscillator.java  v0.1  4 December 2014 2:28:28 PM
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
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ultimate_oscillator
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V03/C04/ULTI.pdf
 *
 * @author Daniel Kuan
 * @version
 */
public class UltimateOscillator extends AbstractIndicator {

  private final int shortPeriod;
  private final int mediumPeriod;
  private final int longPeriod;

  public UltimateOscillator() {
    this(SEVEN, FOURTEEN, TWENTY_EIGHT);
  }

  public UltimateOscillator(final int shortPeriod, final int mediumPeriod, final int longPeriod) {
    super(TA_LIB.ultOscLookback(shortPeriod,
                                mediumPeriod,
                                longPeriod));
    throwExceptionIfNegative(shortPeriod,
                             mediumPeriod,
                             longPeriod);

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

    return TA_LIB.ultOsc(start,
                         end,
                         ohlcv.highs(),
                         ohlcv.lows(),
                         ohlcv.closes(),
                         shortPeriod,
                         mediumPeriod,
                         longPeriod,
                         outBegIdx,
                         outNBElement,
                         output);
  }

}
