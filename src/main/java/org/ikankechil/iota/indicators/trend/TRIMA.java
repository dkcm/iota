/**
 * TRIMA.java  v0.2  5 January 2015 7:06:34 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Triangular Moving Average (TRIMA)
 *
 * <p>http://www.traderslibrary.com/pdf/pdf_9818935_y85j.pdf<br>
 * https://www.thebalance.com/triangular-moving-average-tma-description-and-uses-1031203<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TRIMA extends AbstractIndicator {

  private final int sma;

  public TRIMA() {
    this(THIRTY);
  }

  public TRIMA(final int period) {
    super(period, period - ONE);

    sma = (period >> ONE) + ONE;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // TRIMA = SMA(SMA(Price))
    //
    // where SMA period = ceiling[(TRIMA period + 1) / 2]

    final double[] sma1 = sma(values, sma);
    final double[] sma2 = sma(sma1, sma);

    System.arraycopy(sma2, ZERO, output, ZERO, output.length);

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;

//    return TA_LIB.trima(start,
//                        end,
//                        values,
//                        period,
//                        outBegIdx,
//                        outNBElement,
//                        output);
  }

}
