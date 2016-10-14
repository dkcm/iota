/**
 * Divergence.java  v0.3  20 April 2016 7:36:35 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Divergence.Comparators.*;
import static org.ikankechil.iota.indicators.pattern.Extrema.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class Divergence extends AbstractIndicator {

  private final Indicator     topsAndBottoms;
  private final Indicator     indicator;

  private static final double POSITIVE_DIVERGENCE = HUNDRED_PERCENT;
  private static final double NEGATIVE_DIVERGENCE = -HUNDRED_PERCENT;

  private static final String PEAK     = "Peak ";
  private static final String TROUGH   = "Trough ";

  public Divergence(final Indicator indicator) {
    this(indicator, FIVE);
  }

  public Divergence(final Indicator indicator, final int awayPoints) {
    super(indicator.lookback());

    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
    this.indicator = indicator;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final int size = ohlcv.size();

    // generate tops and bottoms
    final List<TimeSeries> tabs = topsAndBottoms.generate(ohlcv);
    final double[] tops = Arrays.copyOfRange(tabs.get(ZERO).values(),
                                             lookback,
                                             size);
    final double[] bottoms = Arrays.copyOfRange(tabs.get(ONE).values(),
                                                lookback,
                                                size);

    // apply indicator
    final TimeSeries indicatorSeries = indicator.generate(ohlcv).get(ZERO);
    final double[] indicatorValues = indicatorSeries.values();

    // detect divergences
    final double[] highs = Arrays.copyOfRange(ohlcv.highs(), lookback, size);
    final double[] topsDivergences = detectTopsDivergences(tops,
                                                           highs,
                                                           indicatorValues);
    final double[] lows = Arrays.copyOfRange(ohlcv.lows(), lookback, size);
    final double[] bottomsDivergences = detectBottomsDivergences(bottoms,
                                                                 lows,
                                                                 indicatorValues);

    final String[] dates = indicatorSeries.dates();

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(PEAK + name, dates, topsDivergences),
                         new TimeSeries(TROUGH + name, dates, bottomsDivergences));
  }

  protected double[] detectTopsDivergences(final double[] tops,
                                           final double[] highs,
                                           final double[] indicatorValues) {
    return detectDivergences(tops, highs, indicatorValues, TOPS);
  }

  protected double[] detectBottomsDivergences(final double[] bottoms,
                                              final double[] lows,
                                              final double[] indicatorValues) {
    return detectDivergences(bottoms, lows, indicatorValues, BOTTOMS);
  }

  private static final double[] detectDivergences(final double[] globalExtrema,
                                                  final double[] localExtrema,
                                                  final double[] indicatorValues,
                                                  final Comparators comparator) {
    final double[] divergence = new double[indicatorValues.length];

    for (int p = nextExtremum(globalExtrema, NOT_FOUND), c = NOT_FOUND;
         (c = nextExtremum(globalExtrema, p)) > NOT_FOUND;
         p = c) {
      divergence[c] = comparator.divergence(globalExtrema,
                                            localExtrema,
                                            indicatorValues,
                                            p,
                                            c);
    }

    return divergence;
  }

  static enum Comparators {
    TOPS {
      @Override
      public double divergence(final double[] globalExtrema,
                               final double[] localExtrema,
                               final double[] indicatorValues,
                               final int previous,
                               final int current) {
        final double hp = globalExtrema[previous];    // previous top
        final double hc = globalExtrema[current];     // current top

        final double ip = indicatorValues[previous];  // previous top
        final double ic = indicatorValues[current];   // current top

        return (hp < hc && ip > ic) ? POSITIVE_DIVERGENCE : NEGATIVE_DIVERGENCE;
      }
    },
    BOTTOMS {
      @Override
      public double divergence(final double[] globalExtrema,
                               final double[] localExtrema,
                               final double[] indicatorValues,
                               final int previous,
                               final int current) {
        final double lp = globalExtrema[previous];    // previous bottom
        final double lc = globalExtrema[current];     // current bottom

        final double ip = indicatorValues[previous];  // previous bottom
        final double ic = indicatorValues[current];   // current bottom

        return (lp > lc && ip < ic) ? POSITIVE_DIVERGENCE : NEGATIVE_DIVERGENCE;
      }
    };

    public abstract double divergence(final double[] globalExtrema,
                                      final double[] localExtrema,
                                      final double[] indicatorValues,
                                      final int previous,
                                      final int current);

    // Classic Divergence
    // Although a positive divergence is a strong signal of price reversal, an
    // even stronger signal occurs when the high at the current peak is higher
    // than the high at the previous peak (Hc > Hp) and the indicator value at
    // the current peak is lower than the indicator value at the previous peak
    // (Ic < Ip). This is the classic divergence most often illustrated in
    // technical analysis literature.
  }

}
