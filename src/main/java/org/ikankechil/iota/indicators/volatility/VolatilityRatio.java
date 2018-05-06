/**
 * VolatilityRatio.java  v0.2  3 October 2016 7:17:00 pm
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import static org.ikankechil.iota.indicators.volatility.TR.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Volatility Ratio by Jack Schwager
 *
 * <p>References:
 * <li>http://user42.tuxfamily.org/chart/manual/Volatility-Ratio.html<br>
 * <li>http://www.investopedia.com/ask/answers/031115/what-volatility-ratio-formula-and-how-it-calculated.asp<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VolatilityRatio extends AbstractIndicator {

  private final MaximumPrice max;
  private final MinimumPrice min;

  private static final TR    TR = new TR();

  public VolatilityRatio() {
    this(FOURTEEN);
  }

  public VolatilityRatio(final int period) {
    super(period, period);

    max = new MaximumPrice(period);
    min = new MinimumPrice(period);
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

    final double[] maxs = max.generate(new TimeSeries(EMPTY, ohlcv.dates(), ohlcv.highs())).get(ZERO).values();
    final double[] mins = min.generate(new TimeSeries(EMPTY, ohlcv.dates(), ohlcv.lows())).get(ZERO).values();

    for (int i = ZERO, m = i + ONE; i < nDayTR.length; ++i, ++m) {
      nDayTR[i] = trueRange(maxs[m], mins[m], ohlcv.close(i));
    }

    return nDayTR;
  }

}
