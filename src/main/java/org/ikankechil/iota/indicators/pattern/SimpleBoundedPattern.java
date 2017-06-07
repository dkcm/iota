/**
 * SimpleBoundedPattern.java  v0.1  7 June 2017 11:03:23 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
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
import org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;

/**
 * A chart pattern bounded by upper and lower trendlines.
 *
 * <p>Examples:
 * <li>Triangles
 * <li>Wedges
 * <li>Channels
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class SimpleBoundedPattern extends AbstractIndicator {

  private final Indicator     trendlines;
  private final int           vicinity;

  protected static final int  ENDPOINT_VICINITY = TWENTY;
  private static final double PARALLEL          = 0.01;

  private static final String TOPS              = " Tops";
  private static final String BOTTOMS           = " Bottoms";

  public SimpleBoundedPattern(final int awayPoints, final double thresholdPercentage, final TrendSlopes upper, final TrendSlopes lower) {
    this(awayPoints, thresholdPercentage, upper, lower, ENDPOINT_VICINITY);
  }

  /**
   *
   *
   *
   * @param awayPoints
   * @param thresholdPercentage
   * @param upper
   * @param lower
   * @param endpointVicinity number of OHLCV bars the heads and tails of the
   *          trendlines forming the pattern must be in the vicinity of
   */
  public SimpleBoundedPattern(final int awayPoints, final double thresholdPercentage, final TrendSlopes upper, final TrendSlopes lower, final int endpointVicinity) {
    this(new Trendlines(awayPoints,
                        thresholdPercentage,
                        upper,
                        lower),
         endpointVicinity);
  }

  SimpleBoundedPattern(final Trendlines trendlines) {
    this(trendlines, ENDPOINT_VICINITY);
  }

  SimpleBoundedPattern(final Trendlines trendlines, final int endpointVicinity) {
    super(ZERO);
    throwExceptionIfNegative(endpointVicinity);
    if (trendlines == null) {
      throw new NullPointerException();
    }

    this.trendlines = trendlines;
    vicinity = endpointVicinity;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    // generate trendlines
    final List<TimeSeries> trendlineTimeSeries = trendlines.generate(ohlcv, start);
    final TrendlineTimeSeries upperTrendlineTimeSeries = (TrendlineTimeSeries) trendlineTimeSeries.get(ZERO);
    final TrendlineTimeSeries lowerTrendlineTimeSeries = (TrendlineTimeSeries) trendlineTimeSeries.get(ONE);

    // locate and draw patterns
    final List<TimeSeries> patterns = draw(upperTrendlineTimeSeries,
                                           lowerTrendlineTimeSeries,
                                           ohlcv.dates());

    logger.info(GENERATED_FOR, name, ohlcv);
    return patterns;
  }

  /**
   * Locate and draw pattern.
   *
   * @param upperTrendlineTimeSeries
   * @param lowerTrendlineTimeSeries
   * @param dates
   * @return
   */
  protected List<TimeSeries> draw(final TrendlineTimeSeries upperTrendlineTimeSeries,
                                  final TrendlineTimeSeries lowerTrendlineTimeSeries,
                                  final String[] dates) {
    final double[] patternTops = new double[upperTrendlineTimeSeries.size()];
    final double[] patternBottoms = new double[patternTops.length];
    Arrays.fill(patternTops, Double.NaN);
    Arrays.fill(patternBottoms, Double.NaN);
    final List<Trendline> patternTopTrendlines = new ArrayList<>();
    final List<Trendline> patternBottomTrendlines = new ArrayList<>();

    final double[] upperTrends = upperTrendlineTimeSeries.values();
    final double[] lowerTrends = lowerTrendlineTimeSeries.values();

    // locate and draw patterns
    final List<Trendline> candidates = selectCandidates(upperTrendlineTimeSeries, lowerTrendlineTimeSeries);
    final List<Trendline> counterparts = selectCounterparts(upperTrendlineTimeSeries, lowerTrendlineTimeSeries);

    for (final Trendline candidate : candidates) {
      if (hasPotential(candidate)) {
        // search for complementary counterpart only when candidate trendline has potential
        for (final Trendline counterpart : counterparts) {
          if (isComplementary(candidate, counterpart)) {
            // draw pattern tops
            final Trendline top = selectTop(candidate, counterpart);
            draw(patternTops, patternTopTrendlines, upperTrends, top);

            // draw pattern bottoms
            final Trendline bottom = selectBottom(candidate, counterpart);
            draw(patternBottoms, patternBottomTrendlines, lowerTrends, bottom);

            logger.info("New {} formed by top ({}) and bottom ({})", name, top, bottom);
          }
        }
      }
    }

    return Arrays.asList(new TrendlineTimeSeries(name + TOPS,
                                                 dates,
                                                 patternTops,
                                                 patternTopTrendlines),
                         new TrendlineTimeSeries(name + BOTTOMS,
                                                 dates,
                                                 patternBottoms,
                                                 patternBottomTrendlines));
  }

  /**
   *
   *
   * @param upperTrendlineTimeSeries
   * @param lowerTrendlineTimeSeries
   * @return
   */
  protected abstract List<Trendline> selectCandidates(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries);

  /**
   *
   *
   * @param upperTrendlineTimeSeries
   * @param lowerTrendlineTimeSeries
   * @return
   */
  protected abstract List<Trendline> selectCounterparts(final TrendlineTimeSeries upperTrendlineTimeSeries, final TrendlineTimeSeries lowerTrendlineTimeSeries);

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
   * @return true when candidate <code>trendline</code> exhibits pattern
   *         characteristics
   */
  protected abstract boolean hasPotential(final Trendline trendline);

  /**
   *
   *
   * @param candidate
   * @param counterpart
   * @return true when candidate and counterpart <code>trendline</code>s come
   *         together to form a pattern
   */
  protected boolean isComplementary(final Trendline candidate, final Trendline counterpart) {
    // trendline heads / tails are in the vicinity
    return (Math.abs(candidate.x1() - counterpart.x1()) <= vicinity) &&
           (Math.abs(candidate.x2() - counterpart.x2()) <= vicinity);
  }

  /**
   *
   *
   * @param candidate
   * @param counterpart
   * @return
   */
  protected boolean isParallel(final Trendline candidate, final Trendline counterpart) {
    return Math.abs(candidate.m() - counterpart.m()) <= PARALLEL;
  }

  private static final void draw(final double[] patterns,
                                 final List<Trendline> patternTrendlines,
                                 final double[] trends,
                                 final Trendline trendline) {
    final int x1 = trendline.x1();
    System.arraycopy(trends,
                     x1,
                     patterns,
                     x1,
                     trendline.x2() - x1);
    patternTrendlines.add(trendline);
  }

}
