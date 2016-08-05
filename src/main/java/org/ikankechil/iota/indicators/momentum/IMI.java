/**
 * IMI.java  v0.1 11 February 2016 2:35:50 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Intraday Momentum Index (IMI) by Tushar Chande and Stanley Kroll
 * <p>
 * http://www.fxf1.com/english-books/Chande%20Kroll%20-%20The%20New%20Technical%20Trader.pdf
 * http://www.technicalindicators.net/indicators-technical-analysis/173-imi-intraday-momentum-index
 * http://technical.traders.com/tradersonline/display.asp?art=609
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IMI extends AbstractIndicator {

  public IMI() {
    this(FOURTEEN);
  }

  public IMI(final int period) {
    super(period, period - ONE);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // IMI = 100 x (Gains / (Gains + Losses))

    final double[] opens = ohlcv.opens();
    final double[] closes = ohlcv.closes();

    // compute gains and losses
    final int size = closes.length;
    final double[] gains = new double[size];
    final double[] losses = new double[size];
    for (int i = ZERO; i < size; ++i) {
      final double change = closes[i] - opens[i];
      if (change > ZERO) {  // close > open
        gains[i] = change;
      }
      else {                // close < open
        losses[i] = -change;
      }
    }

    final double[] sumGains = sum(period, gains);
    final double[] sumLosses = sum(period, losses);

    for (int i = ZERO; i < output.length; ++i) {
      output[i] = imi(sumGains[i], sumLosses[i]);
    }

    int v = ZERO;
    double gain = gains[v];
    double loss = losses[v];
    for (; ++v < period; ) {
      gain += gains[v];
      loss += losses[v];
    }

    // compute indicator (first value)
    int i = ZERO;
    double imi = imi(gain, loss);
    output[i] = imi;

    // compute gains and losses (subsequent value)
    for (; v < size; ++v) {
      gain += gains[v] - gains[i];
      loss += losses[v] - losses[i];

      // compute indicator (subsequent values)
      output[++i] = imi = imi(gain, loss);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double imi(final double gains, final double losses) {
    return HUNDRED_PERCENT * (gains / (gains + losses));
  }

}
