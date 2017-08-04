/**
 * Trendlines.java  v0.8  19 January 2016 4:00:07 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.iota.indicators.pattern.Extrema.*;
import static org.ikankechil.iota.indicators.pattern.Trendlines.TrendSlopes.*;
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
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:chart_analysis:trend_lines<br>
 * <li>https://stockcharts.com/articles/dancing/2014/11/trendlines.html<br>
 * <li>http://thepatternsite.com/uptrendlines.html<br>
 * <li>http://thepatternsite.com/trenddown.html<br>
 * <li>http://thepatternsite.com/TrendlineMirrors.html<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.8
 */
public class Trendlines extends AbstractIndicator {

  private final Indicator     topsAndBottoms;
  private final double        breakoutThreshold;
  private final double        runawayThreshold;
  private final TrendSlopes   upper;
  private final TrendSlopes   lower;

  private static final int    GAP                = TWO;
  private static final int    BREAKOUT_THRESHOLD = THREE;
  private static final double RUNAWAY_THRESHOLD  = Double.POSITIVE_INFINITY;

  private static final String UPPER_TRENDLINE    = "Upper Trendline";
  private static final String LOWER_TRENDLINE    = "Lower Trendline";

  /**
   *
   *
   * @param awayPoints
   */
  public Trendlines(final int awayPoints) {
    this(awayPoints, BREAKOUT_THRESHOLD);
  }

  /**
   *
   *
   * @param awayPoints
   * @param breakoutThresholdPercentage trendline breakout penetration threshold (%)
   */
  public Trendlines(final int awayPoints, final double breakoutThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, RUNAWAY_THRESHOLD);
  }

  /**
   *
   *
   * @param awayPoints
   * @param breakoutThresholdPercentage trendline breakout penetration threshold (%)
   * @param runawayThresholdPercentage price run-away threshold (%)
   */
  public Trendlines(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage) {
    this(awayPoints, breakoutThresholdPercentage, runawayThresholdPercentage, DOWN, UP);
  }

  Trendlines(final int awayPoints, final double breakoutThresholdPercentage, final double runawayThresholdPercentage, final TrendSlopes upper, final TrendSlopes lower) {
    super(ZERO);
    throwExceptionIfNegative(breakoutThresholdPercentage, runawayThresholdPercentage);
    if (upper == null || lower == null) {
      throw new NullPointerException();
    }

    breakoutThreshold = breakoutThresholdPercentage / HUNDRED_PERCENT;
    runawayThreshold = runawayThresholdPercentage / HUNDRED_PERCENT;
    topsAndBottoms = new TopsAndBottoms(awayPoints, null, false);
    this.upper = upper;
    this.lower = lower;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    // generate tops and bottoms
    final List<TimeSeries> tab = topsAndBottoms.generate(ohlcv);
    final double[] tops = tab.get(ZERO).values();
    final double[] bottoms = tab.get(ONE).values();

    // draw support and resistance (lower and upper) trendlines
    final String[] dates = ohlcv.dates();
    final TimeSeries resistanceTrendlines = drawTrendlines(RESISTANCE, upper, tops, dates);
    final TimeSeries supportTrendlines = drawTrendlines(SUPPORT, lower, bottoms, dates);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(resistanceTrendlines, supportTrendlines);
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

    public boolean isPracticallyHorizontal() {
      return FLAT.isRightDirection(m);
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
    RESISTANCE {
      @Override
      public boolean isUnbroken(final double y, final double fx) {
        return (y <= fx);
      }

      @Override
      public boolean isDecisivelyBroken(final double y, final double fx, final double threshold) {
        return (y > fx * (ONE + threshold));
      }

      @Override
      public boolean isRunaway(final double y, final double fx, final double threshold) {
        return (y <= fx * (ONE - threshold));
      }

      @Override
      public String toString() {
        return UPPER_TRENDLINE;
      }
    },
    SUPPORT {
      @Override
      public boolean isUnbroken(final double y, final double fx) {
        return (y >= fx);
      }

      @Override
      public boolean isDecisivelyBroken(final double y, final double fx, final double threshold) {
        return (y < fx * (ONE - threshold));
      }

      @Override
      public boolean isRunaway(final double y, final double fx, final double threshold) {
        return (y >= fx * (ONE + threshold));
      }

      @Override
      public String toString() {
        return LOWER_TRENDLINE;
      }
    };

    /**
     * Checks that actual value has not exceeded expected value.
     *
     * @param y actual value
     * @param fx expected value
     * @return <code>true</code> if <code>y</code> has not exceeded
     *         <code>fx</code>
     */
    public abstract boolean isUnbroken(final double y, final double fx);

    /**
     * Checks if actual value has exceeded expected value by a threshold.
     *
     * @param y actual value
     * @param fx expected value
     * @param threshold
     * @return <code>true</code> if <code>y</code> has exceeded <code>fx</code>
     *         by <code>threshold</code>
     */
    public abstract boolean isDecisivelyBroken(final double y, final double fx, final double threshold);

    /**
     *
     *
     * @param y actual value
     * @param fx expected value
     * @param threshold
     * @return
     */
    public abstract boolean isRunaway(final double y, final double fx, final double threshold);

  }

  enum TrendSlopes {
    UP {
      @Override
      public boolean isRightDirection(final double gradient) {
        return (gradient > ZERO) && (gradient < Double.POSITIVE_INFINITY);
      }
    },
    DOWN {
      @Override
      public boolean isRightDirection(final double gradient) {
        return (gradient < ZERO) && (gradient > Double.NEGATIVE_INFINITY);
      }
    },
    FLAT {
      @Override
      public boolean isRightDirection(final double gradient) {
        return (gradient >= -PRACTICALLY_HORIZONTAL) && (gradient <= PRACTICALLY_HORIZONTAL);
      }
    };

    private static final double PRACTICALLY_HORIZONTAL = 0.01; // TODO make this configurable

    /**
     * Checks that the trend is headed in the right direction.
     *
     * @param gradient
     * @return <code>true</code> if <code>gradient</code> is according to trend
     */
    public abstract boolean isRightDirection(final double gradient);

  }

  private final TimeSeries drawTrendlines(final Trends trend, final TrendSlopes slope, final double[] extrema, final String[] dates) {
    final double[] lines = new double[extrema.length];
    Arrays.fill(lines, Double.NaN);
    final List<Trendline> trendlines = new ArrayList<>();

    Trendline trendline = null;
    for (int x1 = nextExtremum(extrema, NOT_FOUND), x2 = NOT_FOUND;
         (x2 = nextExtremum(extrema, x1)) > NOT_FOUND;
         x1 = x2) {
      if (trendline == null) {
        final double y1 = extrema[x1];
        final double y2 = extrema[x2];
        // trendline in general right direction => start of trend
        if (slope.isRightDirection(gradient(x1, y1, x2, y2))) {
          trendline = new Trendline(x1, y1, x2, y2);
          trendlines.add(trendline);
          logger.debug("New {} trend from ({}, {}) to ({}, {})", slope, x1, y1, x2, y2);
        }
      }
      else {
        final double y2 = extrema[x2];
        final double fx2 = trendline.f(x2);

        // trendline decisively broken => end of trend
        if (trend.isDecisivelyBroken(y2, fx2, breakoutThreshold)) {
          trendline.broken(true);
          draw(trendline, lines, x2 - GAP); // extend trendline less gap
          trendline = null;
          logger.debug("End of {} trend at / before ({}, {})", slope, x2, y2);
        }
        // trendline indecisively penetrated, still unconfirmed and in the right
        // direction => amend trendline
        else if (!trend.isUnbroken(y2, fx2) &&
                 !trendline.isConfirmed() &&
                 slope.isRightDirection(gradient(trendline.x1(), trendline.y1(), x2, y2))) {
          trendline.x2y2(x2, y2);
          logger.debug("Amending {} trend at ({}, {})", slope, x2, y2);
        }
        // runaway prices => end trend prematurely
        else if (trend.isRunaway(y2, fx2, runawayThreshold)) {
          draw(trendline, lines, x2 - GAP); // extend trendline less gap
          trendline = null;
          logger.debug("Runaway prices in unbroken {} trend at / before ({}, {})", slope, x2, y2);
        }
        else {
          logger.debug("{} trend holds at ({}, {})", slope, x2, y2);
        }
      }
    }

    // reached end of chart
    if (trendline != null) {
      draw(trendline, lines, lines.length - ONE); // extend trendline to the end
      logger.debug("On-going unbroken {} trend at ({}, {})", slope, trendline.x2(), trendline.y2());
    }

    return new TrendlineTimeSeries(trend.toString(), dates, lines, trendlines);

    // Possibilities:
    // 1. decisively broken -> draw
    // 2. indecisively penetrated
    //    a) unconfirmed -> amend
    //    b) confirmed -> do nothing
    // 3. runaway -> draw
    // 4. unbroken -> do nothing

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

  static final void draw(final Trendline line, final double[] trendlines) {
    final int x1 = line.x1();
    final int x2 = line.x2();
    interpolate(x1,
                trendlines[x1] = line.y1(),
                x2,
                trendlines[x2] = line.y2(),
                trendlines);
  }

}
