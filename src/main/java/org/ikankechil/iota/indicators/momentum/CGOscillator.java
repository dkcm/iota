/**
 * CGOscillator.java  v0.4  7 July 2015 3:41:47 PM
 *
 * Copyright © 2015-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.EhlersFilter;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Centre of Gravity Oscillator (CGOscillator) by John Ehlers
 *
 * <p>References:
 * <li>http://www.mesasoftware.com/papers/TheCGOscillator.pdf<br>
 * <li>http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf<br>
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C05/088CENT.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class CGOscillator extends EhlersFilter {

  private final double        zeroCounter;
  private final String        signalName;

  private static final String SIGNAL = " Signal";

  public CGOscillator() {
    this(TEN);
  }

  public CGOscillator(final int period) {
    super(period, period, period - ONE);

    zeroCounter = (period + ONE) * HALF; // (period + 1) / 2
    signalName = name + SIGNAL;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    final TimeSeries cg = super.generate(ohlcv, start).get(ZERO);
    final double[] cgo = new double[cg.size()];
    // shift forwards by one
    System.arraycopy(cg.values(), ZERO, cgo, ONE, cgo.length - ONE);
    return Arrays.asList(cg,
                         new TimeSeries(signalName, cg.dates(), cgo));
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
    // where Price is median price

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      double sumProduct = ZERO;
      double sumPrice = ZERO;
      for (int j = period, v = i; j > ZERO; --j, ++v) { // count backwards
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

}
