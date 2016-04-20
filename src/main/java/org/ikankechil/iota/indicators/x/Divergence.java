/**
 * Divergence.java v0.1 20 April 2016 7:36:35 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.x;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.TopsAndBottoms;

/**
 *
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Divergence extends AbstractIndicator {

  private final Indicator ohlcTopsAndBottoms;
  private final Indicator indicatorTopsAndBottoms;

  public Divergence(final Indicator indicator, final int awayPoints) {
    super(ZERO);

    ohlcTopsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
    indicatorTopsAndBottoms = new TopsAndBottoms(awayPoints, indicator, false);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    // generate tops and bottoms
    final List<TimeSeries> ohlcTabs = ohlcTopsAndBottoms.generate(ohlcv);
    final List<TimeSeries> indicatorTabs = indicatorTopsAndBottoms.generate(ohlcv);

    final double[] ohlcTops = ohlcTabs.get(ZERO).values();
    final double[] ohlcBottoms = ohlcTabs.get(ONE).values();
    final double[] indicatorTops = indicatorTabs.get(ZERO).values();
    final double[] indicatorBottoms = indicatorTabs.get(ONE).values();

    final double[] topsDivergences = topsDivergence(ohlcTops, indicatorTops);
    final double[] bottomsDivergences = bottomsDivergence(ohlcBottoms, indicatorBottoms);

    final String[] dates = ohlcv.dates();

    return Arrays.asList(new TimeSeries(name, dates, topsDivergences),
                         new TimeSeries(name, dates, bottomsDivergences));
  }

  private double[] topsDivergence(final double[] ohlcTops, final double[] indicatorTops) {
    // TODO Auto-generated method stub
    return null;
  }

  private double[] bottomsDivergence(final double[] ohlcBottoms, final double[] indicatorBottoms) {
    // TODO Auto-generated method stub
    return null;
  }

  // Classic Divergence
  // Although a positive divergence is a strong signal of price reversal, an
  // even stronger signal occurs when the high at the current peak is higher
  // than the high at the previous peak (Hc > Hp) and the indicator value at the
  // current peak is lower than the indicator value at the previous peak (Ic <
  // Ip). This is the classic divergence most often illustrated in technical
  // analysis literature.

}
