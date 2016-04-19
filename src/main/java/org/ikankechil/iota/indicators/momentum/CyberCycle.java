/**
 * CyberCycle.java v0.2 7 July 2015 8:00:08 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Cyber Cycle Indicator by John Ehlers
 * <p>
 * http://xa.yimg.com/kq/groups/17324418/1380195797/name/cybernetic+analysis+for+stocks+and+futures+cutting-edge+dsp+technology+to+improve+your+trading+(0471463078).pdf
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CyberCycle extends AbstractIndicator {

  // high-pass filter coefficients: derivatives of alpha
  private final double c1;
  private final double c2;
  private final double c3;

  public CyberCycle() {
    this(0.07);
  }

  public CyberCycle(final int period) {
    // alpha = 2 / (period + 1)
    this(TWO / (double) (period + ONE));
  }

  public CyberCycle(final double alpha) {
    // alpha = 2 / (period + 1)
    super(EIGHT, SEVEN);

    throwExceptionIfNegative(alpha);

    final double alpha2 = ONE - (HALF * alpha);
    c1 = alpha2 * alpha2;

    final double alpha3 = (ONE - alpha);
    c2 = TWO * alpha3;
    c3 = alpha3 * alpha3;
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    final double[] values = new double[ohlcv.size()];
    // mid-point of high and low
    for (int i = ZERO; i < values.length; ++i) {
      values[i] = (ohlcv.high(i) + ohlcv.low(i)) * HALF;
    }

    return compute(start,
                   end,
                   values,
                   outBegIdx,
                   outNBElement,
                   output);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    //  Inputs: Price((H+L)/2),
    //  alpha(.07);
    //  Vars: Smooth(0),
    //  Cycle(0);
    //  Smooth = (Price + 2*Price[1] + 2*Price[2] + Price[3])/6;
    //  Cycle = (1 - .5*alpha)*(1 - .5*alpha)*(Smooth - 2*Smooth[1] + Smooth[2]) + 2*(1 - alpha)*Cycle[1] - (1 - alpha)*(1 - alpha)*Cycle[2];
    //  If currentbar < 7 then Cycle = (Price - 2*Price[1] + Price[2]) / 4;
    //  Plot1(Cycle, "Cycle");
    //  Plot2(Cycle[1], "Trigger");

    // smooth prices
    final double[] smooth = new double[values.length - THREE];
    for (int i1 = start, i2 = i1 + ONE, i3 = i2 + ONE, i4 = i3 + ONE;
         i4 < values.length;
         ++i1, ++i2, ++i3, ++i4) {
      smooth[i1] = (values[i1] + ((values[i2] + values[i3]) * TWO) + values[i4]) * SIXTH;
    }

    // compute indicator
    for (int i1 = ZERO, i2 = i1 + ONE, i3 = i2 + ONE;
         i3 < output.length;
         ++i1, ++i2, ++i3) {
      // high-pass filter
      output[i3] = ((c1 * ((smooth[i3] - (TWO * smooth[i2])) + smooth[i1]))
                 + (c2 * output[i2]))
                 - (c3 * output[i1]);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
