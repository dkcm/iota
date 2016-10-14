/**
 * VolatilityRatio.java  v0.1  3 October 2016 7:17:00 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volatility Ratio by Jack Schwager.
 *
 * <p>http://user42.tuxfamily.org/chart/manual/Volatility-Ratio.html<br>
 * http://www.investopedia.com/ask/answers/031115/what-volatility-ratio-formula-and-how-it-calculated.asp<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolatilityRatio extends AbstractIndicator {

  private static final TR TR = new TR();

  public VolatilityRatio() {
    this(FOURTEEN);
  }

  public VolatilityRatio(final int period) {
    super(period, period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // VR = TR / n-day TR
    final TimeSeries tr = TR.generate(ohlcv).get(ZERO);
    final double[] nDayTR = nDayTrueRange(ohlcv);

    // compute indicator
    for (int i = ZERO, j = period - ONE; i < output.length; ++i, ++j) {
      output[i] = tr.value(j) / nDayTR[i];
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private final double[] nDayTrueRange(final OHLCVTimeSeries ohlcv) {
    final double[] nDayTR = new double[ohlcv.size() - lookback];
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    for (int i = ZERO, from = i + ONE, to = from + period;
         i < nDayTR.length;
         ++i, ++from, ++to) {
      final double close = ohlcv.close(i);
      final double max = Math.max(close, max(highs, from, to));
      final double min = Math.min(close, min(lows, from, to));

      nDayTR[i] = (max - min);
    }

    return nDayTR;
  }

}
