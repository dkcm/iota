/**
 * HeadAndShoulders.java  v0.2  27 January 2016 2:10:38 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;
import static org.ikankechil.iota.indicators.pattern.Extrema.Comparators.*;
import static org.ikankechil.iota.indicators.pattern.Trendlines.Trends.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.Extrema.Comparators;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trendline;
import org.ikankechil.iota.indicators.pattern.Trendlines.Trends;

/**
 * Head and Shoulders Tops and Bottoms
 *
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:chart_patterns:head_and_shoulders_top_reversal<br>
 * <li>http://www.chartpatterns.com/headandshoulders.htm<br>
 * <li>http://thepatternsite.com/hst.html<br>
 * <li>http://thepatternsite.com/hsb.html<br>
 * <li>http://www.investopedia.com/university/charts/charts2.asp<br>
 * <li>https://www.incrediblecharts.com/technical/head_and_shoulders.php<br>
 * <li>https://en.wikipedia.org/wiki/Head_and_shoulders_(chart_pattern)<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class HeadAndShoulders extends AbstractIndicator {

  private final Indicator     topsAndBottoms;
  private final double        threshold;

  private static final String HEAD_AND_SHOULDERS_TOPS    = "Head and Shoulders Tops";
  private static final String HEAD_AND_SHOULDERS_BOTTOMS = "Head and Shoulders Bottoms";

  public HeadAndShoulders(final int awayPoints) {
    this(awayPoints, THREE);
  }

  public HeadAndShoulders(final int awayPoints, final double thresholdPercentage) {
    super(ZERO);
    throwExceptionIfNegative(thresholdPercentage);

    threshold = thresholdPercentage / HUNDRED_PERCENT;
    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Algorithm:
    // 1. Locate neckline -- a pair of bottoms / tops that are practically level
    // 2. See that they are sandwiched by a trio of tops / bottoms
    // 3. See that shoulders are practically level and that head is perceptibly
    //    above / below shoulders

    throwExceptionIfShort(ohlcv);

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // draw head and shoulders tops and bottoms (kilroy bottoms)
    final double[] necklines = drawHeadAndShoulders(TOPS, SUPPORT, tops, bottoms, ohlcv.lows());
    final double[] kilroys = drawHeadAndShoulders(BOTTOMS, RESISTANCE, bottoms, tops, ohlcv.highs());

    final String[] dates = ohlcv.dates();

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(HEAD_AND_SHOULDERS_TOPS, dates, necklines),
                         new TimeSeries(HEAD_AND_SHOULDERS_BOTTOMS, dates, kilroys));
  }

  private final double[] drawHeadAndShoulders(final Comparators comparator,
                                              final Trends trend,
                                              final double[] heads,
                                              final double[] necks,
                                              final double[] prices) {
    final double[] necklines = new double[prices.length];
    Arrays.fill(necklines, Double.NaN);

    // locate necklines
    int neckLeft = nextExtremum(necks, NOT_FOUND);
    int necklineLeft = nextExtremum(necks, neckLeft);
    int necklineRight = nextExtremum(necks, necklineLeft);
    int neckRight = nextExtremum(necks, necklineRight);

    while (neckRight > NOT_FOUND) {
      final Trendline neckline = new Trendline(necklineLeft,
                                               necks[necklineLeft],
                                               necklineRight,
                                               necks[necklineRight]);

      if (isNeckline(neckLeft, neckline, neckRight, necks, comparator)) {
        final int head = nextExtremum(heads, necklineLeft); // locate head

        if (isHeadExists(necklineLeft, head, necklineRight)) {
          final int leftShoulder = previousExtremum(heads, necklineLeft);
          final int rightShoulder = nextExtremum(heads, necklineRight);

          if (isHeadExists(neckLeft, leftShoulder, necklineLeft) &&
              isHeadExists(necklineRight, rightShoulder, neckRight) &&
              isHeadOverShoulders(leftShoulder, head, rightShoulder, heads, comparator)) {
            final int breakout = locateNecklineBreakout(neckline, trend, rightShoulder, prices);
            if (breakout > NOT_FOUND) {
              neckline.x1y1(leftShoulder, neckline.f(leftShoulder));
              Trendlines.draw(neckline, necklines);
              logger.info("Neckline located: {}", neckline);
            }
          }
        }
      }

      // locate next neck
      neckLeft = necklineLeft;
      necklineLeft = necklineRight;
      neckRight = nextExtremum(necks, necklineRight = neckRight);
    }

    return necklines;

    // The Head-and-Shoulders (from Edwards and Magee)
    //
    // The typical or, if you will, the ideal Head-and-Shoulders Top consists of:
    //
    // A. A strong rally, climaxing a more or less extensive advance, on which
    //    trading volume becomes very heavy, followed by a Minor Recession on
    //    which volume runs considerably less than it did during the days of
    //    rise and at the Top. This is the "left shoulder."
    // B. Another high-volume advance which reaches a higher level than the top
    //    of the left shoulder, and then another reaction on less volume which
    //    takes prices down to somewhere near the bottom level of the preceding
    //    recession, somewhat lower perhaps or somewhat higher, but, in any
    //    case, below the top of the left shoulder. This is the “Head.”
    // C. A third rally, but this time on decidedly less volume than accompanied
    //    the formation of either the left shoulder or the head, which fails to
    //    reach the height of the head before another decline sets in. This is
    //    the "right shoulder."
    // D. Finally, decline of prices in this third recession down through a line
    //    (the "neckline") drawn across the Bottoms of the reactions between the
    //    left shoulder and head, and the head and right shoulder, respectively,
    //    and a close below that line by an amount approximately equivalent to
    //    3% of the stock’s market price. This is the "confirmation" or
    //    "breakout."
    //
    // Note that each and every item cited in A, B, C, and D is essential to a
    // valid Head-and-Shoulders Top Formation. The lack of any one of them casts
    // in doubt the forecasting value of the pattern. In naming them, we have
    // left the way clear for the many variations that occur (for no two Head-
    // and-Shoulders are exactly alike) and have included only the features
    // which must be present if we are to depend upon the pattern as signaling
    // an important Reversal of Trend.
  }

  private static final boolean isNeckline(final int neckLeft,
                                          final Trendline neckline,
                                          final int neckRight,
                                          final double[] necks,
                                          final Comparators comparator) {
    return neckline.isPracticallyHorizontal() &&
           comparator.isExtremum(neckline.y1(), necks[neckLeft]) &&
           comparator.isExtremum(neckline.y2(), necks[neckRight]);
  }

  private static final boolean isHeadExists(final int neckLeft, final int head, final int neckRight) {
    // head flanked by neck
    return (neckLeft < head && head < neckRight);
  }

  private static final boolean isHeadOverShoulders(final int ls,
                                                   final int h,
                                                   final int rs,
                                                   final double[] heads,
                                                   final Comparators comparator) {
    boolean isHeadOverShoulders = false;
    if (ls > NOT_FOUND && rs > NOT_FOUND) {
      final double head = heads[h];
      final double leftShoulder = heads[ls];
      final double rightShoulder = heads[rs];
      isHeadOverShoulders = comparator.isExtremum(leftShoulder, head, rightShoulder);
    }
    else {
      logger.debug("Non-existent left / right shoulder");
    }
    return isHeadOverShoulders;
  }

  private final int locateNecklineBreakout(final Trendline neckline,
                                           final Trends trend,
                                           final int rightShoulder,
                                           final double[] prices) {
    int breakout = NOT_FOUND;
    for (int i = rightShoulder + ONE; i < prices.length; ++i) {
      final double fx2 = neckline.f(i);
      if (trend.isDecisivelyBroken(prices[i], fx2, threshold)) {
        breakout = i;
        neckline.x2y2(i, fx2);
        neckline.broken(true);
        logger.debug("Neckline breakout at ({}, {})", i, fx2);
        break;
      }
    }
    return breakout;
  }

}
