/**
 * RainbowOscillator.java  v0.2  1 March 2015 5:57:35 pm
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.MinimumMaximumPrice;

/**
 * Rainbow Oscillator by Mel Widner
 *
 * <p>References:
 * <li>https://c.mql5.com/forextsd/forum/77/mel_widner_-_rainbow_charts.pdf<br>
 * <li>https://c.forex-tsd.com/forum/64/rainbow_oscillator_-_formula.pdf<br>
 * <li>https://c.mql5.com/forextsd/forum/64/rainbow_oscillator_-_formula.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RainbowOscillator extends RainbowCharts {

  private final MinimumMaximumPrice minMax;

  private static final String       UPPER_BAND  = "Upper Rainbow Band";
  private static final String       MIDDLE_BAND = "Rainbow Oscillator";
  private static final String       LOWER_BAND  = "Lower Rainbow Band";

  public RainbowOscillator() {
    this(TWO);
  }

  public RainbowOscillator(final int period) {
    super(period);

    minMax = new MinimumMaximumPrice(lookback + ONE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // RANGEA = MAX(AVE1, AVE2, ..., AVE10) - MIN(AVE1, AVE2, ..., AVE10)
    // RANGEC = MAX (CJ) - MIN (CJ)
    // Rainbow bandwidth, RB = 100 * RANGEA / RANGEC
    //
    // AVEA = AVERAGE(AVE1, AVE2, ..., AVE10)
    // Rainbow oscillator, RO = 100 * (C - AVEA) / RANGEC
    //
    // URB = RB
    // LRB = -RB

    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    final double[] indicator = new double[size - lookback];
    final double[] upperBand = new double[indicator.length];
    final double[] lowerBand = new double[upperBand.length];

    final List<TimeSeries> cMinMaxs = minMax.generate(ohlcv);
    final TimeSeries cMins = cMinMaxs.get(ZERO);
    final TimeSeries cMaxs = cMinMaxs.get(ONE);

    final List<TimeSeries> rainbows = super.generate(ohlcv);
    for (int i = ZERO, c = i + lookback; i < indicator.length; ++i, ++c) {
      // compute rainbow max, min and average
      double rMax = Double.NEGATIVE_INFINITY;
      double rMin = Double.POSITIVE_INFINITY;
      double rAverage = ZERO;
      for (final TimeSeries rainbow : rainbows) {
        final double value = rainbow.value(i);
        if (value > rMax) {
          rMax = value;
        }
        if (value < rMin) {
          rMin = value;
        }
        rAverage += value;
      }
      rAverage /= rainbows.size();

      // compute close range
      final double cRange = cMaxs.value(i) - cMins.value(i);

      // compute rainbow bandwidth and oscillator
      final double rb = HUNDRED * (rMax - rMin) / cRange;
      final double ro = HUNDRED * (closes[c] - rAverage) / cRange;

      indicator[i] = ro;
      upperBand[i] = rb;
      lowerBand[i] = -rb;
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);

    return Arrays.asList(new TimeSeries(UPPER_BAND, dates, upperBand),
                         new TimeSeries(MIDDLE_BAND, dates, indicator),
                         new TimeSeries(LOWER_BAND, dates, lowerBand));
  }

}
