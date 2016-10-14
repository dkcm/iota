/**
 * PGO.java  v0.1  16 January 2015 11:21:44 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

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
 * @version 0.1
 */
public class PGO extends AbstractIndicator {

  private final int smaLookback;

  public PGO() {
    this(TWENTY_FIVE);
  }

  public PGO(final int period) {
    super(period, TA_LIB.atrLookback(period));

    smaLookback = TA_LIB.smaLookback(period);
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

    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    // compute SMA
    final double[] sma = new double[size - smaLookback];
    RetCode outcome = TA_LIB.sma(start,
                                 end,
                                 closes,
                                 period,
                                 outBegIdx,
                                 outNBElement,
                                 sma);
    throwExceptionIfBad(outcome, ohlcv);

    // compute ATR TODO confirm SMA, and not EMA, of true range
    final double[] atr = new double[size - lookback];
    outcome = TA_LIB.atr(start,
                         end,
                         ohlcv.highs(),
                         ohlcv.lows(),
                         closes,
                         period,
                         outBegIdx,
                         outNBElement,
                         atr);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    for (int i = ZERO, c = i + lookback, s = i + ONE; i < output.length; ++i, ++c, ++s) {
      output[i] = (closes[c] - sma[s]) / atr[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
