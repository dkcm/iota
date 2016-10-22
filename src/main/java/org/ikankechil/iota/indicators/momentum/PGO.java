/**
 * PGO.java  v0.2  16 January 2015 11:21:44 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.volatility.ATR;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Pretty Good Oscillator (PGO) by Mark Johnson
 *
 * <p>http://user42.tuxfamily.org/chart/manual/Pretty-Good-Oscillator.html<br>
 * http://exceltechnical.web.fc2.com/pgo.html<br>
 *
 *
 * <p>Strategy:
 * Mark Johnson's approach was to use it as a break-out system for longer term
 * trades. If the PGO rises above 3.0 then go long, or below -3.0 then go short,
 * and in both cases exit on returning to zero (which is a close back at the
 * SMA).
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PGO extends AbstractIndicator {

  private final ATR atr;

  public PGO() {
    this(TWENTY_FIVE);
  }

  public PGO(final int period) {
    super(period, period);

    atr = new ATR(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // PGO = (Close - SMA(n)) / ATR(n)

    final double[] closes = ohlcv.closes();

    final double[] sma = sma(closes, period);
    final double[] atrs = atr.generate(ohlcv).get(ZERO).values();

    // compute indicator
    for (int i = ZERO, c = i + lookback, s = i + ONE; i < output.length; ++i, ++c, ++s) {
      output[i] = (closes[c] - sma[s]) / atrs[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
