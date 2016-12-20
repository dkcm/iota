/**
 * RMI.java  0.1  20 December 2016 6:39:20 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.momentum.RSI.*;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Relative Momentum Index (RMI) by Roger Altman
 *
 * <p>http://www.schaeffersresearch.com/content/bgs/2016/03/07/tricks-to-trading-with-the-relative-momentum-index<br>
 * http://www.stockfetcher.com/forums2/Indicators/Relative-Momentum-Index-RMI/96335<br>
 * https://www.tradingview.com/script/UCm7fIvk-FREE-INDICATOR-Relative-Momentum-Index-RMI/<br>
 * http://exceltechnical.web.fc2.com/rmi.html<br>
 * http://www.tradingsolutions.com/functions/RelativeMomentumIndex.html<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMI extends AbstractIndicator {

  private final int    momentum;
  private final double smoothingFactor;

  public RMI() {
    this(TWENTY, FIVE);
  }

  public RMI(final int period, final int momentum) {
    super(period, period + momentum - ONE);
    throwExceptionIfNegative(momentum);

    this.momentum = momentum;
    smoothingFactor = ONE - (ONE / (double) period); // = (period - 1) / period
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // compute average gain and loss (first value)
    double averageGain = ZERO;
    double averageLoss = ZERO;
    for (int vp = ZERO, vc = vp + momentum; vp < period; ++vp, ++vc) {
      final double previous = values[vp];
      final double current = values[vc];
      final double change = current - previous;
      if (change >= ZERO) {
        averageGain += change;
      }
      else {
        averageLoss -= change;
      }
    }

    // compute indicator (first value)
    int i = ZERO;
    double rmi = rsi(averageGain, averageLoss);
    output[i] = rmi;

    // compute average gain and loss (subsequent values)
    for (int vp = period, vc = vp + momentum; vc < values.length; ++vp, ++vc) {
      final double previous = values[vp];
      final double current = values[vc];
      final double change = current - previous;

      if (change >= ZERO) { // gain
        averageGain = averageGain * smoothingFactor + change;
        averageLoss *= smoothingFactor;
      }
      else {                // loss
        averageGain *= smoothingFactor;
        averageLoss = averageLoss * smoothingFactor - change;
      }

      // compute indicator (subsequent values)
      output[++i] = rmi = rsi(averageGain, averageLoss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
