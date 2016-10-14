/**
 * CMO.java  v0.2  8 December 2014 10:15:02 AM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Chande Momentum Oscillator (CMO) by Tushar Chande
 *
 * <p>https://www.incrediblecharts.com/indicators/chande-momentum-oscillator.php<br>
 * https://www.fidelity.com/learning-center/trading-investing/technical-analysis/technical-indicator-guide/cmo<br>
 * http://www.technicalindicators.net/indicators-technical-analysis/144-cmo-chande-momentum-oscillator<br>
 * http://www.motivewave.com/studies/chande_momentum_oscillator.htm<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CMO extends AbstractIndicator {

  public CMO() {
    this(FOURTEEN);
  }

  public CMO(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final double[] values,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. If today's close > yesterday's close
    //    a) Gain = today's close - yesterday's close
    //    b) Loss = 0
    // 2. If today's close < yesterday's close
    //    a) Gain = 0
    //    b) Loss = yesterday's close - today's close
    // 3. Sum all gains and losses over period
    // 4. CMO = 100 * (gains - losses) / (gains + losses)

    // compute gains and losses
    final double[] gains = new double[values.length - ONE];
    final double[] losses = new double[gains.length];
    int v = ZERO;
    double yesterday = values[v];
    for (int i = ZERO; i < gains.length; ++i) {
      final double today = values[++v];

      if (today > yesterday) {
        gains[i] = today - yesterday;
      }
      else if (today < yesterday) {
        losses[i] = yesterday - today;
      }

      // shift forward in time
      yesterday = today;
    }

    // sum gains and losses
    final double[] sumGains = sum(period, gains);
    final double[] sumLosses = sum(period, losses);

    // compute indicator
    for (int i = ZERO; i < output.length; ++i) {
      final double sumGain = sumGains[i];
      final double sumLoss = sumLosses[i];
      output[i] = HUNDRED_PERCENT * (sumGain - sumLoss) / (sumGain + sumLoss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

}
