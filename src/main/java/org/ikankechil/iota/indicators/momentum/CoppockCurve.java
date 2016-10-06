/**
 * CoppockCurve.java  v0.1  21 December 2014 12:09:23 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.trend.WMA;

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
  private final WMA wma;

  public CoppockCurve() {
    this(FOURTEEN, ELEVEN, TEN);
  }

  public CoppockCurve(final int roc1, final int roc2, final int wma) {
    super(wma, Math.max(roc1, roc2) + (wma - ONE));
    throwExceptionIfNegative(roc1, roc2);

    this.roc1 = new ROC(roc1);
    this.roc2 = new ROC(roc2);
    this.wma = new WMA(wma);
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

    // compute RoCs
    final TimeSeries roc1s = roc1.generate(ohlcv).get(ZERO);
    final TimeSeries roc2s = roc2.generate(ohlcv).get(ZERO);

    final int length = Math.min(roc1s.size(), roc2s.size());

    // compute sum of RoCs
    final TimeSeries rocSum = new TimeSeries(EMPTY, length);
    for (int i = ZERO, r1 = roc1s.size() - length, r2 = roc2s.size() - length;
         i < rocSum.size();
         ++i, ++r1, ++r2) {
      rocSum.value(roc1s.value(r1) + roc2s.value(r2), i);
    }

    // compute WMA
    final double[] cc = wma.generate(rocSum).get(ZERO).values();
    System.arraycopy(cc, ZERO, output, ZERO, cc.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
