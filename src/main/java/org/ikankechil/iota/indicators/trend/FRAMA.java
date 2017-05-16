/**
 * FRAMA.java  v0.1  13 October 2016 10:14:29 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static java.lang.Math.*;
import static org.ikankechil.iota.indicators.MaximumPriceIndex.*;
import static org.ikankechil.iota.indicators.MinimumPriceIndex.*;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.HighPrice;
import org.ikankechil.iota.indicators.LowPrice;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Fractal Adaptive Moving Average (FRAMA) by John Ehlers
 *
 * <p>
 * The FRAMA identifies the Fractal Dimension of prices over a specific period
 * and then uses the result to dynamically adapt the smoothing period of an
 * exponential moving average.<br>
 *
 * <p>http://www.mesasoftware.com/papers/FRAMA.pdf<br>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V23/C10/217EHLR.pdf<br>
 * http://etfhq.com/blog/2010/09/30/fractal-adaptive-moving-average-frama/<br>
 * http://etfhq.com/blog/2010/10/09/frama-is-it-effective/<br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class FRAMA extends AbstractIndicator {

  private final int              halfPeriod;
  private final double           inversePeriod;
  private final double           inverseHalfPeriod;
  private final double           w;
  private final MinimumPrice     min;
  private final MaximumPrice     max;

  private static final HighPrice HIGH          = new HighPrice();
  private static final LowPrice  LOW           = new LowPrice();

  private static final double    INVERSE_LOG_2 = ONE / log(TWO);
  private static final double    W             = -4.6;

  public FRAMA() {
    this(SIXTEEN);
  }

  public FRAMA(final int period) {
    this(period, W);
  }

  public FRAMA(final int period, final double w) {
    super(period, period - ONE);
    if ((period % TWO) == ONE) { // odd period
      throw new IllegalArgumentException("Period must be even");
    }

    halfPeriod = period >> ONE;
    inversePeriod = ONE / (double) period;
    inverseHalfPeriod = ONE / (double) halfPeriod;
    this.w = (w < ZERO) ? w : W;
    min = new MinimumPrice(period);
    max = new MaximumPrice(period);
  }

  @Override
  protected RetCode compute(final int start,
                            final int end,
                            final OHLCVTimeSeries ohlcv,
                            final MInteger outBegIdx,
                            final MInteger outNBElement,
                            final double[] output) {
    // Formula:
    // FRAMA = FRAMA[1] + alpha * (Price – FRAMA[1])
    // where
    // FRAMA[1] = value of the FRAMA one bar ago.
    // alpha = e^[W * (D - 1)]
    // D = (log(N1 + N2) - log(N3)) / log(2)
    // N1 = (max - min) / T  (from 0 to  T bars ago)
    // N2 = (max - min) / T  (from T to 2T bars ago)
    // N3 = (max - min) / 2T (from 0 to 2T bars ago)
    // T = FRAMA period, an even number
    // W = -4.6

    final TimeSeries highsSeries = HIGH.generate(ohlcv).get(ZERO);
    final TimeSeries lowsSeries = LOW.generate(ohlcv).get(ZERO);

    final double[] maxs = max.generate(highsSeries).get(ZERO).values();
    final double[] mins = min.generate(lowsSeries).get(ZERO).values();

    final double[] highs = highsSeries.values();
    final double[] lows = lowsSeries.values();

    // first value is price
    int v = lookback;
    final double[] values = ohlcv.closes();
    double previous = values[v++];

    // compute indicator
    for (int i = ZERO, half1 = i + halfPeriod, half2 = i + period;
         i < output.length;
         ++i, ++half1, ++half2, ++v) {
      final double n1High = highs[maxIndex(i, half1, highs)];
      final double n1Low = lows[minIndex(i, half1, lows)];
      final double n2High = highs[maxIndex(half1, half2, highs)];
      final double n2Low = lows[minIndex(half1, half2, lows)];

      // compute fractal dimension
      final double n1 = (n1High - n1Low) * inverseHalfPeriod;
      final double n2 = (n2High - n2Low) * inverseHalfPeriod;
      final double n3 = (maxs[i] - mins[i]) * inversePeriod;
      final double d = fractalDimension(n1, n2, n3);

      // compute FRAMA
      final double alpha = exp(w * (d - ONE));
      output[i] = previous = EMA.ema(alpha, values[v], previous);
    }

    outBegIdx.value = lookback;
    outNBElement.value = output.length;
    return RetCode.Success;
  }

  private static final double fractalDimension(final double n1, final double n2, final double n3) {
    return (log(n1 + n2) - log(n3)) * INVERSE_LOG_2;
  }

}
