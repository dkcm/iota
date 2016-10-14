/**
 * DivergenceIndex.java  v0.2  22 November 2015 10:41:10 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.DivergenceIndex.Comparators.*;
import static org.ikankechil.iota.indicators.pattern.Extrema.*;

import org.ikankechil.iota.indicators.Indicator;

/**
 * Divergence Index by Matt Storz
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C01/QUANTIF.pdf<br>
 * http://www.multicharts.com/support/base/?action=article&id=1758<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DivergenceIndex extends Divergence {

  public DivergenceIndex(final Indicator indicator) {
    this(indicator, FIVE);
  }

  public DivergenceIndex(final Indicator indicator, final int awayPoints) {
    super(indicator, awayPoints);
  }

  @Override
  protected double[] detectTopsDivergences(final double[] tops,
                                           final double[] highs,
                                           final double[] indicatorValues) {
    return detectDivergences(tops, highs, indicatorValues, TOPS);
  }

  @Override
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
        final double hp = globalExtrema[previous];                // previous top
        final double hc = globalExtrema[current];                 // current top
        final double hh = (hc > hp) ? hc : hp;                    // highest high
        final double hl = min(localExtrema, previous, current);   // lowest high

        final double ip = indicatorValues[previous];              // previous top
        final double ic = indicatorValues[current];               // current top
        final double ih = (ic > ip) ? ic : ip;                    // highest high
        final double il = min(indicatorValues, previous, current);// lowest high

        return FIFTY_PERCENT * (((hc - hp) / (hh - hl)) - ((ic - ip) / (ih - il)));
      }
    },
    BOTTOMS {
      @Override
      public double divergence(final double[] globalExtrema,
                               final double[] localExtrema,
                               final double[] indicatorValues,
                               final int previous,
                               final int current) {
        final double lp = globalExtrema[previous];                // previous bottom
        final double lc = globalExtrema[current];                 // current bottom
        final double lh = max(localExtrema, previous, current);   // highest low
        final double ll = (lc < lp) ? lc : lp;                    // lowest low

        final double ip = indicatorValues[previous];              // previous bottom
        final double ic = indicatorValues[current];               // current bottom
        final double ih = max(indicatorValues, previous, current);// highest low
        final double il = (ic < ip) ? ic : ip;                    // lowest low

        return FIFTY_PERCENT * (((ic - ip) / (ih - il)) - ((lc - lp) / (lh - ll)));
      }
    };

    public abstract double divergence(final double[] globalExtrema,
                                      final double[] localExtrema,
                                      final double[] indicatorValues,
                                      final int previous,
                                      final int current);

    // Formula:
    // Peak divergence, Dp = 50 (((Hc - Hp)/(Hh - Hl))-((Ic - Ip)/(Ih - Il)))
    // where
    //  Hc = High at the current peak
    //  Hp = High at the previous peak
    //  Hh = Highest high from the previous to the current peak
    //  Hl = Lowest high from the previous to the current peak
    //  Ic = Indicator value at the current peak
    //  Ip = Indicator value at the previous peak
    //  Ih = Highest indicator value from the previous to the current peak
    //  Il = Lowest indicator value from the previous to the current peak
    //
    // Trough divergence, Dt = 50 (((Ic - Ip)/(Ih - Il))-((Lc - Lp)/(Lh - Ll)))
    // where
    //  Ic = Indicator value at the current trough
    //  Ip = Indicator value at the previous trough
    //  Ih = Highest indicator value from the previous to the current trough
    //  Il = Lowest indicator value from the previous to the current trough
    //  Lc = Low at the current trough
    //  Lp = Low at the previous trough
    //  Lh = Highest low from the previous to the current trough
    //  Ll = Lowest low from the previous to the current trough
    //
    // The more positive the divergence index is, the more likely a price reversal
    // will occur. Negative divergences are often associated with false peaks.
  }

}
