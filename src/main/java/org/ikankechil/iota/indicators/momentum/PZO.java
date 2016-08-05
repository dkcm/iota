/**
 * PZO.java  v0.1  11 December 2015 12:30:38 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Price Zone Oscillator (PZO) by Walid Khalil and David Steckler
 * <p>
 * http://www.highgrowthstock.com/wpstorage/pdf/blog-hgsi-davesteckler-vzo-pzo.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PZO extends AbstractIndicator {

  public PZO() {
    this(FOURTEEN);
  }

  public PZO(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // Price zone oscillator = 100 x (CP / TC)
    // where:
    // CP (Closing position) = X-days EMA (± close) and
    // TC (Total close) = X-days EMA (close)

    final double[] closes = values;
    final int size = closes.length;

    // compute R
    final double[] r = new double[size];
    int c = ZERO;
    double pc = r[c] = closes[c];
    for (; ++c < size; ) {
      final double close = closes[c];
      r[c] = (close > pc) ? close : -close;
      pc = close;
    }

    // compute closing position and total close
    final double[] cp = ema(r, period);
    final double[] tc = ema(closes, period);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      output[i] = HUNDRED_PERCENT * cp[i] / tc[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
