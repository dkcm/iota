/**
 * CGOscillator.java v0.1 7 July 2015 3:41:47 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Centre of Gravity Oscillator (CGOscillator) by John Ehlers
 * <p>
 * http://www.mesasoftware.com/papers/TheCGOscillator.pdf
 * http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C05/088CENT.pdf
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CGOscillator extends EhlersFilter {

  private final double zeroCounter;

  public CGOscillator(final int period) {
    super(period, period - ONE);

    zeroCounter = (period + ONE) * HALF; // (period + 1) / 2
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // CG = sum(Index * Price) / sum(Price)

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      double sumProduct = ZERO;
      double sumPrice = ZERO;
      for (int j = period, v = i; j > ZERO; --j, ++v) {
        final double value = values[v];
        sumProduct += j * value;
        sumPrice += value;
      }

      if (sumPrice != ZERO) {
        output[i] = zeroCounter - (sumProduct / sumPrice);
      }
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  @Override
  protected double[] coefficients(final int index, final double... values) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void cleanUp() {
    // do nothing
  }

}
