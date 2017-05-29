/**
 * Triangles.java  v0.3  8 January 2016 10:56:34 AM
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
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * Triangles pattern recognition algorithm.  Covers triangles and wedges.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public abstract class Triangles extends AbstractIndicator {

  private final Indicator     trendlines;
  private final int           ohlcvBarsErrorMargin;

  private static final int    BARS_ERROR_MARGIN = TWENTY;

  private static final String TOPS              = " Tops";
  private static final String BOTTOMS           = " Bottoms";

  /**
   *
   *
   * @param trendlines
   */
  public Triangles(final Trendlines trendlines) {
    this(trendlines, BARS_ERROR_MARGIN);
  }

  /**
   *
   *
   * @param trendlines
   * @param ohlcvBarsErrorMargin number of OHLCV bars the heads and tails of the
   *          trendlines forming the triangle must be in the vicinity of
   */
  public Triangles(final Trendlines trendlines, final int ohlcvBarsErrorMargin) {
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

  /**
   * Locate and draw triangles.
   *
   * @param downTrendlineTimeSeries
   * @param upTrendlineTimeSeries
   * @param triangleTops
   * @param triangleBottoms
   * @param triangleTopTrendlines
   * @param triangleBottomTrendlines
   */
  protected void drawTriangles(final TrendlineTimeSeries downTrendlineTimeSeries,
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
    final List<Trendline> candidates = selectCandidates(downTrendlines, upTrendlines);
    final List<Trendline> counterparts = selectCounterparts(downTrendlines, upTrendlines);

    for (final Trendline candidate : candidates) {
      if (hasPotential(candidate)) {
        // search for complementary counterpart only when candidate trendline has potential
        for (final Trendline counterpart : counterparts) {
          if (isComplementary(candidate, counterpart)) {
            // draw triangle tops
            final Trendline top = selectTop(candidate, counterpart);
            draw(triangleTops, triangleTopTrendlines, downTrends, top);

            // draw triangle bottoms
            final Trendline bottom = selectBottom(candidate, counterpart);
            draw(triangleBottoms, triangleBottomTrendlines, upTrends, bottom);

            logger.info("New {} formed by top ({}) and bottom ({})", name, top, bottom);
          }
        }
      }
    }
  }

  /**
   *
   *
   * @param downTrendlines
   * @param upTrendlines
   * @return
   */
  protected abstract List<Trendline> selectCandidates(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines);

  /**
   *
   *
   * @param downTrendlines
   * @param upTrendlines
   * @return
   */
  protected abstract List<Trendline> selectCounterparts(final List<Trendline> downTrendlines, final List<Trendline> upTrendlines);

  /**
   *
   *
   * @param candidate
   * @param counterpart
   * @return
   */
  protected abstract Trendline selectTop(final Trendline candidate, final Trendline counterpart);

  /**
   *
   *
   * @param candidate
   * @param counterpart
   * @return
   */
  protected abstract Trendline selectBottom(final Trendline candidate, final Trendline counterpart);

  /**
   *
   *
   * @param trendline candidate
   * @return true when candidate <code>trendline</code> exhibits
   *         <code>Triangles</code> subclass characteristics
   */
  protected abstract boolean hasPotential(final Trendline trendline);

  /**
   *
   *
   * @param candidate
   * @param counterpart
   * @return
   */
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    // trendline heads / tails are in the vicinity
    return (Math.abs(candidate.x1() - counterpart.x1()) <= ohlcvBarsErrorMargin) &&
           (Math.abs(candidate.x2() - counterpart.x2()) <= ohlcvBarsErrorMargin);
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
