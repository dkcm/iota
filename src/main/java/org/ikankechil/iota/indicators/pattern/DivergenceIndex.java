/**
 * DivergenceIndex.java	v0.1	22 November 2015 10:41:10 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;

import org.ikankechil.iota.indicators.Indicator;

/**
 * Divergence Index by Matt Storz
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V14/C01/QUANTIF.pdf
 * http://www.multicharts.com/support/base/?action=article&id=1758
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DivergenceIndex extends Divergence {

  public DivergenceIndex(final Indicator indicator, final int awayPoints) {
    super(indicator, awayPoints);
  }

  @Override
  protected double[] detectTopsDivergences(final double[] tops,
                                           final double[] highs,
                                           final double[] indicatorValues) {
    final double[] divergence = new double[indicatorValues.length];

    for (int p = nextExtremum(tops, NOT_FOUND), c = NOT_FOUND;
         (c = nextExtremum(tops, p)) > NOT_FOUND;
         p = c) {
      final double hp = tops[p];                    // previous top
      final double hc = tops[c];                    // current top
      final double hh = (hc > hp) ? hc : hp;        // highest high
      final double hl = min(highs, p, c);           // lowest high

      final double ip = indicatorValues[p];         // previous top
      final double ic = indicatorValues[c];         // current top
      final double ih = (ic > ip) ? ic : ip;        // highest high
      final double il = min(indicatorValues, p, c); // lowest high

      divergence[c] = FIFTY_PERCENT * (((hc - hp) / (hh - hl)) - ((ic - ip) / (ih - il)));
    }

    return divergence;
  }

  @Override
  protected double[] detectBottomsDivergences(final double[] bottoms,
                                              final double[] lows,
                                              final double[] indicatorValues) {
    final double[] divergence = new double[indicatorValues.length];

    for (int p = nextExtremum(bottoms, NOT_FOUND), c = NOT_FOUND;
         (c = nextExtremum(bottoms, p)) > NOT_FOUND;
         p = c) {
      final double lp = bottoms[p];                 // previous bottom
      final double lc = bottoms[c];                 // current bottom
      final double lh = max(lows, p, c);            // highest low
      final double ll = (lc < lp) ? lc : lp;        // lowest low

      final double ip = indicatorValues[p];         // previous bottom
      final double ic = indicatorValues[c];         // current bottom
      final double ih = max(indicatorValues, p, c); // highest low
      final double il = (ic < ip) ? ic : ip;        // lowest low

      divergence[c] = FIFTY_PERCENT * (((ic - ip) / (ih - il)) - ((lc - lp) / (lh - ll)));
    }

    return divergence;
  }

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
