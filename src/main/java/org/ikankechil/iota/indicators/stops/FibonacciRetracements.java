/**
 * FibonacciRetracements.java  v0.2  16 May 2017 6:57:49 pm
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.TopsAndBottoms;

/**
 * Fibonacci Retracements
 *
 * <p>Plots the following retracements:
 * <ol>
 * <li> 0.0%
 * <li> 23.6%
 * <li> 38.2%
 * <li> 50.0%
 * <li> 61.8%
 * <li> 100.0%
 * </ol>
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:fibonacci_retracemen<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class FibonacciRetracements extends AbstractIndicator {

  private final Indicator topsAndBottoms;

  public FibonacciRetracements() {
    this(FIVE);
  }

  public FibonacciRetracements(final int awayPoints) {
    super(ZERO);

    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
  }

  private enum Retracements {
    _0(ZERO), _236(0.236), _382(0.382), _50(HALF), _618(0.618), _100(ONE);

    private final double level;
    private final String seriesName;

    Retracements(final double level) {
      this.level = level;
      seriesName = String.format("Fibonacci %.1f%%", level * HUNDRED_PERCENT);
    }

    /**
     *
     *
     * @param y1 initial price
     * @param y2 final price
     * @return Fibonacci retracement level
     */
    public double level(final double y1, final double y2) {
      return y2 + ((y1 - y2) * level);
    }

  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // draw fibonacci retracements
    final Map<Retracements, TimeSeries> fibonacciRetracements = drawRetracements(tops, bottoms, ohlcv.dates());

    logger.info(GENERATED_FOR, name, ohlcv);
    return new ArrayList<>(fibonacciRetracements.values());
  }

  private static final Map<Retracements, TimeSeries> drawRetracements(final double[] tops,
                                                                      final double[] bottoms,
                                                                      final String[] dates) {
    final Map<Retracements, TimeSeries> fibonacciRetracements = new EnumMap<>(Retracements.class);

    // initialise
    final int size = tops.length;
    for (final Retracements retracement : Retracements.values()) {
      final double[] timeSeries = new double[size];
      Arrays.fill(timeSeries, Double.NaN);
      fibonacciRetracements.put(retracement,
                                new TimeSeries(retracement.seriesName,
                                               dates,
                                               timeSeries));
      logger.debug("{} created", retracement.seriesName);
    }

    // locate and draw fibonacci retracements
    for (int i = NOT_FOUND; i < size; ++i) {
      final int top = nextExtremum(tops, i);
      final int bottom = nextExtremum(bottoms, i);

      if (top > NOT_FOUND && bottom > NOT_FOUND) {
        final int x1 = Math.min(top, bottom);
        final int x2 = Math.max(top, bottom);
        draw(fibonacciRetracements,
             x1,
             i = x2,
             bottoms[bottom],
             tops[top]);
      }
    }

    return fibonacciRetracements;
  }

  private static final void draw(final Map<Retracements, TimeSeries> fibonacciRetracements,
                                 final int x1,
                                 final int x2,
                                 final double y1,
                                 final double y2) {
    logger.debug("Fibonacci retracement from ({}, {}) to ({}, {})", x1, y1, x2, y2);
    for (final Entry<Retracements, TimeSeries> fibonacciRetracement : fibonacciRetracements.entrySet()) {
      final Retracements retracement = fibonacciRetracement.getKey();
      final double level = retracement.level(y1, y2);
      final TimeSeries timeSeries = fibonacciRetracement.getValue();
      for (int i = x1; i < x2; ++i) {
        timeSeries.value(level, i);
      }
      logger.debug("{}: {}", retracement.seriesName, level);
    }
  }

}
