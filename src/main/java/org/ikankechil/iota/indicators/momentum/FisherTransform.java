/**
 * FisherTransform.java  v0.2  16 January 2015 10:14:56 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MedianPrice;
import org.ikankechil.iota.indicators.MinimumMaximumPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Fisher Transform by John Ehlers
 *
 * <p>https://www.mesasoftware.com/papers/UsingTheFisherTransform.pdf<br>
 * http://exceltechnical.web.fc2.com/ft.html<br>
 * http://user42.tuxfamily.org/chart/manual/Fisher-Transform.html<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C11/130fish.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V20/C11/142tips.pdf<br>
 * https://www.linnsoft.com/techind/fisher-transform-fish<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class FisherTransform extends AbstractIndicator {

  private final MinimumMaximumPrice minMax;

  private static final MedianPrice  MEDIAN = new MedianPrice();
  private static final double       LIMIT  = 0.999;

  public FisherTransform() {
    this(TEN);
  }

  public FisherTransform(final int period) {
    super(period, period - ONE);

    minMax = new MinimumMaximumPrice(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // 1. price = (high + low) / 2
    // 2. raw = 2 * (price - Ndaylow) / (Ndayhigh - Ndaylow) - 1
    // 3. smoothed = EMA[5] of raw
    // 4. fisher = EMA[3] of ln((1 + smoothed) / (1 - smoothed))

    // compute median prices
    final TimeSeries medPrices = MEDIAN.generate(ohlcv).get(ZERO);

    // compute median price highs and lows
    final List<TimeSeries> minMaxs = minMax.generate(medPrices);
    final double[] medLows = minMaxs.get(ZERO).values();
    final double[] medHighs = minMaxs.get(ONE).values();

    // compute indicator
    double previousValue = ZERO;
    double previousFisher = ZERO;
    for (int i = ZERO, j = i + lookback; i < output.length; ++i, ++j) {
      final double medPrice = medPrices.value(j);
      final double high = medHighs[i];
      final double low = medLows[i];

      // compute smoothed raw
      final double value = TWO_THIRDS * ((medPrice - low) / (high - low) - HALF + previousValue);

      // limit / "clamp" to avoid infinity and NaN
      previousValue = limit(value);

      // compute fisher transform
      previousFisher = output[i] = HALF * (fisher(previousValue) + previousFisher);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double fisher(final double x) {
    return Math.log((ONE + x) / (ONE - x));
  }

  private static final double limit(final double d) {
    return (d >  LIMIT) ?  LIMIT :
           (d < -LIMIT) ? -LIMIT : d;
  }

}
