/**
 * Divergence.java v0.1 20 April 2016 7:36:35 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Divergence extends AbstractIndicator {

  private final Indicator topsAndBottoms;
  private final Indicator indicator;

  public Divergence(final Indicator indicator, final int awayPoints) {
    super(ZERO);

    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
    this.indicator = indicator;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final int offset = indicator.lookback();
    final int size = ohlcv.size();

    // generate tops and bottoms
    final List<TimeSeries> tabs = topsAndBottoms.generate(ohlcv);
    final double[] tops = Arrays.copyOfRange(tabs.get(ZERO).values(),
                                             offset,
                                             size);
    final double[] bottoms = Arrays.copyOfRange(tabs.get(ONE).values(),
                                                offset,
                                                size);

    // apply indicator
    final TimeSeries indicatorSeries = indicator.generate(ohlcv).get(ZERO);
    final double[] indicatorValues = indicatorSeries.values();

    // detect divergences
    final double[] highs = Arrays.copyOfRange(ohlcv.highs(), offset, size);
    final double[] topsDivergences = detectTopsDivergences(tops,
                                                           highs,
                                                           indicatorValues);
    final double[] lows = Arrays.copyOfRange(ohlcv.lows(), offset, size);
    final double[] bottomsDivergences = detectBottomsDivergences(bottoms,
                                                                 lows,
                                                                 indicatorValues);

    final String[] dates = indicatorSeries.dates();

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name, dates, topsDivergences),
                         new TimeSeries(name, dates, bottomsDivergences));
  }

  /**
   *
   * @param tops
   * @param highs
   * @param indicatorValues
   * @return
   */
  protected double[] detectTopsDivergences(final double[] tops,
                                           final double[] highs,
                                           final double[] indicatorValues) {
    final double[] divergence = new double[indicatorValues.length];

    for (int p = nextExtremum(tops, NOT_FOUND), c = NOT_FOUND;
         (c = nextExtremum(tops, p)) > NOT_FOUND;
         p = c) {
      final double hp = tops[p];            // previous top
      final double hc = tops[c];            // current top

      final double ip = indicatorValues[p]; // previous top
      final double ic = indicatorValues[c]; // current top

      divergence[c] = (hp < hc && ip > ic) ? HUNDRED_PERCENT : -HUNDRED_PERCENT;
    }

    return divergence;
  }

  /**
   *
   * @param bottoms
   * @param lows
   * @param indicatorValues
   * @return
   */
  protected double[] detectBottomsDivergences(final double[] bottoms,
                                              final double[] lows,
                                              final double[] indicatorValues) {
    final double[] divergence = new double[indicatorValues.length];

    for (int p = nextExtremum(bottoms, NOT_FOUND), c = NOT_FOUND;
         (c = nextExtremum(bottoms, p)) > NOT_FOUND;
         p = c) {
      final double lp = bottoms[p];         // previous bottom
      final double lc = bottoms[c];         // current bottom

      final double ip = indicatorValues[p]; // previous bottom
      final double ic = indicatorValues[c]; // current bottom

      divergence[c] = (lp > lc && ip < ic) ? HUNDRED_PERCENT : -HUNDRED_PERCENT;
    }

    return divergence;
  }

  // Classic Divergence
  // Although a positive divergence is a strong signal of price reversal, an
  // even stronger signal occurs when the high at the current peak is higher
  // than the high at the previous peak (Hc > Hp) and the indicator value at the
  // current peak is lower than the indicator value at the previous peak (Ic <
  // Ip). This is the classic divergence most often illustrated in technical
  // analysis literature.

}
