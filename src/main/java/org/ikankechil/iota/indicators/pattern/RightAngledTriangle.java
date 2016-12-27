/**
 * RightAngledTriangle.java  0.1  23 December 2016 12:49:31 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Abstract class representing ascending and descending triangles.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class RightAngledTriangle extends AbstractIndicator {

  private final Trendlines    trendlines;
  private final int           ohlcvBarsErrorMargin;

  private static final int    BARS_ERROR_MARGIN = TWENTY;

  private static final String TOPS              = " Tops";
  private static final String BOTTOMS           = " Bottoms";

  /**
   *
   *
   * @param trendlines
   */
  public RightAngledTriangle(final Trendlines trendlines) {
    this(trendlines, BARS_ERROR_MARGIN);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin
   *          number of OHLCV bars the heads and tails of the trendlines forming
   *          the triangle must be in the vicinity of
   */
  public RightAngledTriangle(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
    super(ZERO);
    throwExceptionIfNegative(ohlcvBarsErrorMargin);
    if (trendlines == null) {
      throw new NullPointerException();
    }

    this.trendlines = trendlines;
    this.ohlcvBarsErrorMargin = ohlcvBarsErrorMargin;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    // generate trendlines
    final List<TimeSeries> trendlineTimeSeries = trendlines.generate(ohlcv, start);
    final TrendlineTimeSeries downTrendlineTimeSeries = (TrendlineTimeSeries) trendlineTimeSeries.get(ZERO);
    final TrendlineTimeSeries upTrendlineTimeSeries = (TrendlineTimeSeries) trendlineTimeSeries.get(ONE);

    // locate and draw triangles
    final double[] triangleTops = new double[downTrendlineTimeSeries.size()];
    final double[] triangleBottoms = new double[triangleTops.length];
    final List<Trendline> triangleTopTrendlines = new ArrayList<>();
    final List<Trendline> triangleBottomTrendlines = new ArrayList<>();
    drawTriangles(downTrendlineTimeSeries,
                  upTrendlineTimeSeries,
                  triangleTops,
                  triangleBottoms,
                  triangleTopTrendlines,
                  triangleBottomTrendlines);

    final String[] dates = ohlcv.dates();

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TrendlineTimeSeries(name + TOPS,
                                                 dates,
                                                 triangleTops,
                                                 triangleTopTrendlines),
                         new TrendlineTimeSeries(name + BOTTOMS,
                                                 dates,
                                                 triangleBottoms,
                                                 triangleBottomTrendlines));
  }

  void drawTriangles(final TrendlineTimeSeries downTrendlineTimeSeries,
                     final TrendlineTimeSeries upTrendlineTimeSeries,
                     final double[] triangleTops,
                     final double[] triangleBottoms,
                     final List<Trendline> triangleTopTrendlines,
                     final List<Trendline> triangleBottomTrendlines) {
    Arrays.fill(triangleTops, Double.NaN);
    Arrays.fill(triangleBottoms, Double.NaN);

    final List<Trendline> downTrendlines = downTrendlineTimeSeries.trendlines();
    final List<Trendline> upTrendlines = upTrendlineTimeSeries.trendlines();
    final double[] downTrends = downTrendlineTimeSeries.values();
    final double[] upTrends = upTrendlineTimeSeries.values();

    // locate and draw triangles
    final List<Trendline> counterparts = selectCounterparts(downTrendlines, upTrendlines);
    for (final Trendline trendline : selectPotentiallyFlatTrendlines(downTrendlines, upTrendlines)) {
      if (trendline.isPracticallyHorizontal()) {
        // search for complementary counterpart only when trendline is flat
        for (final Trendline counterpart : counterparts) {
          if (isComplementary(trendline, counterpart) &&
              !counterpart.isPracticallyHorizontal()) {
            // draw triangle tops
            final Trendline top = selectTop(trendline, counterpart);
            draw(triangleTops, triangleTopTrendlines, downTrends, top);

            // draw triangle bottoms
            final Trendline bottom = selectBottom(trendline, counterpart);
            draw(triangleBottoms, triangleBottomTrendlines, upTrends, bottom);

            logger.info("New {} formed by top ({}) and bottom ({})", name, top, bottom);
          }
        }
      }
    }
  }

  abstract List<Trendline> selectPotentiallyFlatTrendlines(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines);

  abstract List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines);

  abstract Trendline selectTop(final Trendline trendline, final Trendline counterpart);

  abstract Trendline selectBottom(final Trendline trendline, final Trendline counterpart);

  private final boolean isComplementary(final Trendline trendline1,
                                        final Trendline trendline2) {
    return (Math.abs(trendline1.x1() - trendline2.x1()) <= ohlcvBarsErrorMargin) &&
           (Math.abs(trendline1.x2() - trendline2.x2()) <= ohlcvBarsErrorMargin);
  }

  private static final void draw(final double[] triangles,
                                 final List<Trendline> triangleTrendlines,
                                 final double[] trends,
                                 final Trendline trendline) {
    final int x1 = trendline.x1();
    System.arraycopy(trends,
                     x1,
                     triangles,
                     x1,
                     trendline.x2() - x1);
    triangleTrendlines.add(trendline);
  }

}
