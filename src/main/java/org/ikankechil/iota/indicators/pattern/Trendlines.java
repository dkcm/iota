/**
 * Trendlines.java  v0.5  19 January 2016 4:00:07 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;
import static org.ikankechil.iota.indicators.pattern.Trendlines.Trends.*;
import static org.ikankechil.util.NumberUtility.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.TrendlineTimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 * Up and Down Trendlines
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class Trendlines extends AbstractIndicator {

  private final Indicator     topsAndBottoms;
  private final double        threshold;

  private static final int    GAP            = TWO;

  private static final String DOWN_TRENDLINE = "Down Trendline";
  private static final String UP_TRENDLINE   = "Up Trendline";

  /**
   *
   *
   * @param awayPoints
   */
  public Trendlines(final int awayPoints) {
    this(awayPoints, THREE);
  }

  /**
   *
   *
   * @param awayPoints
   * @param thresholdPercentage penetration threshold (%)
   */
  public Trendlines(final int awayPoints, final double thresholdPercentage) {
    super(ZERO);
    throwExceptionIfNegative(thresholdPercentage);

    threshold = thresholdPercentage / HUNDRED_PERCENT;
    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // draw up and down trendlines
    final List<Trendline> downTrends = new ArrayList<>();
    final List<Trendline> upTrends = new ArrayList<>();
    final double[] downTrendlines = drawTrendlines(DOWN, tops, downTrends);
    final double[] upTrendlines = drawTrendlines(UP, bottoms, upTrends);

    final String[] dates = ohlcv.dates();

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TrendlineTimeSeries(DOWN_TRENDLINE, dates, downTrendlines, downTrends),
                         new TrendlineTimeSeries(UP_TRENDLINE, dates, upTrendlines, upTrends));
  }

  public static class Trendline {

    // Cartesian co-ordinates
    private int     x1;
    private double  y1;
    private int     x2;
    private double  y2;

    private double  m;  // gradient
    private double  c;  // y-intercept

    private boolean trendConfirmed;
    private boolean trendBroken;

    public Trendline(final int x1, final double y1, final int x2, final double y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;

      m = gradient(x1, y1, x2, y2);
      c = intercept(x1, y1, m);

      trendConfirmed = false;
      trendBroken = false;
    }

    public int x1() {
      return x1;
    }

    public double y1() {
      return y1;
    }

    public int x2() {
      return x2;
    }

    public double y2() {
      return y2;
    }

    public void x1y1(final int x, final double y) {
      if (x1 != x || y1 != y) {
        x1 = x;
        y1 = y;
        mc();
      }
    }

    public void x2y2(final int x, final double y) { // trend confirmed
      if (x2 != x || y2 != y) {
        x2 = x;
        y2 = y;
        mc();
        trendConfirmed = true;
      }
    }

    private final void mc() {
      m = gradient(x1, y1, x2, y2);
      c = intercept(x1, y1, m);
    }

    public double m() {
      return m;
    }

    public double c() {
      return c;
    }

    public double f(final double x) {
      return (m * x) + c;
    }

    public boolean isConfirmed() {
      return trendConfirmed;
    }

    public void broken(final boolean broken) {
      trendBroken = broken;
    }

    public boolean isBroken() {
      return trendBroken;
    }

    @Override
    public String toString() {
      return String.format("%s (%d, %f) -> (%d, %f): y = %fx + %f",
                           this.getClass().getSimpleName(),
                           x1,
                           y1,
                           x2,
                           y2,
                           m,
                           c);
    }

  }

  enum Trends {
    UP {
      @Override
      public boolean isUnbroken(final double y, final double f) {
        return (y >= f);
      }

      @Override
      public boolean isDecisivelyBroken(final double y, final double f, final double threshold) {
        return (y < f * (ONE - threshold));
      }

      @Override
      public boolean isRightDirection(final double gradient) {
        return (gradient > ZERO);
      }
    },
    DOWN {
      @Override
      public boolean isUnbroken(final double y, final double f) {
        return (y <= f);
      }

      @Override
      public boolean isDecisivelyBroken(final double y, final double f, final double threshold) {
        return (y > f * (ONE + threshold));
      }

      @Override
      public boolean isRightDirection(final double gradient) {
        return (gradient < ZERO);
      }
    };

    /**
     * Checks that actual value has not exceeded expected value.
     *
     * @param y actual value
     * @param f expected value
     * @return <code>true</code> if <code>y</code> has not exceeded
     *         <code>f</code>
     */
    public abstract boolean isUnbroken(final double y, final double f);

    /**
     * Checks if actual value has exceeded expected value by a threshold.
     *
     * @param y actual value
     * @param f expected value
     * @param threshold
     * @return <code>true</code> if <code>y</code> has exceeded <code>f</code>
     *         by <code>threshold</code>
     */
    public abstract boolean isDecisivelyBroken(final double y, final double f, final double threshold);

    /**
     * Checks that the trend is headed in the right direction.
     *
     * @param gradient
     * @return <code>true</code> if <code>gradient</code> is according to trend
     */
    public abstract boolean isRightDirection(final double gradient);

  }

  private final double[] drawTrendlines(final Trends trend, final double[] extrema, final List<Trendline> lines) {
    final double[] trendlines = new double[extrema.length];
    Arrays.fill(trendlines, Double.NaN);

    Trendline line = null;
    for (int x1 = nextExtremum(extrema, NOT_FOUND), x2 = NOT_FOUND;
         (x2 = nextExtremum(extrema, x1)) > NOT_FOUND;
         x1 = x2) {
      if (line == null) {
        final double y1 = extrema[x1];
        final double y2 = extrema[x2];
        // trendline in general right direction => start of trend
        if (trend.isRightDirection(gradient(x1, y1, x2, y2))) {
          line = new Trendline(x1, y1, x2, y2);
          lines.add(line);
          logger.debug("New {} trend", trend);
        }
      }
      else {
        final double y2 = extrema[x2];
        final double fx2 = line.f(x2);

        // trendline decisively broken => end of trend
        if (trend.isDecisivelyBroken(y2, fx2, threshold)) {
          line.broken(true);
          draw(line, trendlines, x2 - GAP); // extend trendline less gap
          line = null;
          logger.debug("End of {} trend", trend);
        }
        // trendline indecisively penetrated, still unconfirmed and in the right
        // direction => amend trendline
        else if (!trend.isUnbroken(y2, fx2) &&
                 !line.isConfirmed() &&
                 trend.isRightDirection(gradient(line.x1(), line.y1(), x2, y2))) {
          line.x2y2(x2, y2);
          logger.debug("Amending {} trend", trend);
        }
        else {
          logger.debug("{} trend holds", trend);
        }
      }
    }
    if (line != null) {
      draw(line, trendlines, trendlines.length - ONE); // extend trendline to the end
    }

    return trendlines;

    // Possibilities:
    // 1. decisively broken -> draw
    // 2. indecisively penetrated
    //    a) unconfirmed -> amend
    //    b) confirmed -> do nothing
    // 3. unbroken -> do nothing

    // Amendment of Trendlines (from Edwards and Magee)
    //
    // If the original trendlines depended on only two points, i.e., on the
    // first two Bottoms across which it was projected, and the indecisive
    // penetration occurred when prices returned to it for the third time, the
    // line had better be redrawn across the original first and the new third
    // Bottoms. Or, you may find in such cases that a new line drawn across the
    // second and third Bottoms works better; if the first Bottom was a Reversal
    // Day with its closing level well above the low of its range, you may find
    // that this new line, when extended back, strikes just about at that
    // closing level.
    //
    // If, on the other hand, the original trendline has been "tested" one or
    // more times after it was drawn -- if, that is, a third and perhaps a fourth
    // Bottom have formed on it without penetrating it and have thus "confirmed"
    // it -- then the subsequent indecisive penetration may be disregarded and
    // the original line considered to be still in effect.
  }

  private static final void draw(final Trendline line, final double[] trendlines, final int x2) {
    line.x2y2(x2, line.f(x2)); // extend trendline to x2
    draw(line, trendlines);
  }

  private static final void draw(final Trendline line, final double[] trendlines) {
    final int x1 = line.x1();
    final int x2 = line.x2();
    interpolate(x1,
                trendlines[x1] = line.y1(),
                x2,
                trendlines[x2] = line.y2(),
                trendlines);
  }

}
