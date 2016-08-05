/**
 * CoppockCurve.java  v0.1  21 December 2014 12:09:23 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Coppock Curve by Edwin "Sedge" Coppock
 * <p>
 * http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:coppock_curve
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CoppockCurve extends AbstractIndicator {

  private final ROC roc1;
  private final ROC roc2;

  public CoppockCurve() {
    this(FOURTEEN, ELEVEN, TEN);
  }

  public CoppockCurve(final int roc1, final int roc2, final int wma) {
    super(wma, Math.max(roc1, roc2) + TA_LIB.wmaLookback(wma));
    throwExceptionIfNegative(roc1, roc2);

    this.roc1 = new ROC(roc1);
    this.roc2 = new ROC(roc2);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Coppock Curve = 10-period WMA of (14-period RoC + 11-perod RoC)

    final double[] roc1s = roc1.generate(ohlcv).get(ZERO).values();
    final double[] roc2s = roc2.generate(ohlcv).get(ZERO).values();

    final int length = Math.min(roc1s.length, roc2s.length);

    final double[] rocSum = new double[length];
    for (int i = ZERO, r1 = roc1s.length - length, r2 = roc2s.length - length;
         i < rocSum.length;
         ++i, ++r1, ++r2) {
      rocSum[i] = roc1s[r1] + roc2s[r2];
    }

    return TA_LIB.wma(ZERO,
                      rocSum.length - ONE,
                      rocSum,
                      period,
                      outBegIdx,
                      outNBElement,
                      output);
  }

}
