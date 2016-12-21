/**
 * Extrema.java  v0.3  7 January 2016 9:53:20 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import static org.ikankechil.util.NumberUtility.*;

import java.util.ArrayList;
import java.util.List;

import org.ikankechil.iota.ExtremumTimeSeries;
import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class Extrema extends AbstractIndicator {

  private final int           awayPoints;
  private final Indicator     indicator;
  private final boolean       isInterpolate;
  private final Comparators[] comparators;

  public static final int     NOT_FOUND    = -ONE;
  private static final double NOT_EXTREMUM = ZERO;

  public Extrema(final int awayPoints, final boolean interpolate, final Comparators... comparators) {
    this(awayPoints, null, interpolate, comparators);
  }

  public Extrema(final int awayPoints, final Indicator indicator, final boolean interpolate, final Comparators... comparators) {
    super(ZERO);
    throwExceptionIfNegative(awayPoints);

    this.awayPoints = awayPoints;
    this.indicator = indicator;
    isInterpolate = interpolate;
    this.comparators = comparators;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    throwExceptionIfShort(ohlcv);

    final List<TimeSeries> topsAndBottoms = new ArrayList<>();
    if (indicator != null) {
      // find indicator tops and bottoms
      topsAndBottoms.addAll(indicator.generate(ohlcv));

      final TimeSeries indicatorSeries = topsAndBottoms.get(ZERO);
      final double[] indicatorValues = indicatorSeries.values();
      final String[] dates = indicatorSeries.dates();

      for (final Comparators comparator : comparators) {
        topsAndBottoms.add(findExtremes(indicatorValues, dates, comparator));
      }
    }
    else {
      // find OHLC tops and bottoms
      final String[] dates = ohlcv.dates();
      for (final Comparators comparator : comparators) {
        final double[] values;
        switch (comparator) {
          case TOPS:
            values = ohlcv.highs();
            break;

          case BOTTOMS:
          default:
            values = ohlcv.lows();
            break;
        }
        topsAndBottoms.add(findExtremes(values, dates, comparator));
      }
    }

    logger.info(GENERATED_FOR, name, ohlcv);
    return topsAndBottoms;
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    throwExceptionIfShort(series);

    final double[] values = series.values();
    final String[] dates = series.dates();
    final List<TimeSeries> topsAndBottoms = new ArrayList<>();
    for (final Comparators comparator : comparators) {
      topsAndBottoms.add(findExtremes(values, dates, comparator));
    }

    logger.info(GENERATED_FOR, name, series);
    return topsAndBottoms;
  }

  public enum Comparators {
    TOPS("Tops") {
      /**
       *
       *
       * @param before a value before <code>now</code>
       * @param now the current value
       * @param after a value after <code>now</code>
       * @return true if <code>before</code> and <code>after</code> are both less
       *         than <code>now</code>
       */
      @Override
      public boolean isExtremum(final double before, final double now, final double after) {
        return (before <= now) && (now > after);
      }

      /**
       *
       *
       * @param before
       * @param after
       * @return true if <code>before</code> is more than <code>after</code>
       */
      @Override
      public boolean isExtremum(final double before, final double after) {
        return (before > after);
      }
    },
    BOTTOMS("Bottoms") {
      /**
       *
       *
       * @param before a value before <code>now</code>
       * @param now the current value
       * @param after a value after <code>now</code>
       * @return true if <code>before</code> and <code>after</code> are both more
       *         than <code>now</code>
       */
      @Override
      public boolean isExtremum(final double before, final double now, final double after) {
        return (before >= now) && (now < after);
      }

      /**
       *
       *
       * @param before
       * @param after
       * @return true if <code>before</code> is less than <code>after</code>
       */
      @Override
      public boolean isExtremum(final double before, final double after) {
        return (before < after);
      }
    };

    final String seriesName;

    private Comparators(final String seriesName) {
      this.seriesName = seriesName;
    }

    /**
     * Determines whether <code>now</code> is potentially an extremum.
     *
     * @param before a value before <code>now</code>
     * @param now the current value
     * @param after a value after <code>now</code>
     * @return true if <code>now</code> is potentially an extremum
     */
    public abstract boolean isExtremum(final double before, final double now, final double after);

    /**
     * Determines whether <code>before</code> is potentially an extremum.
     *
     * @param before
     * @param after
     * @return true if <code>before</code> is potentially an extremum
     */
    public abstract boolean isExtremum(final double before, final double after);

  }

  private final ExtremumTimeSeries findExtremes(final double[] values,
                                                final String[] dates,
                                                final Comparators comparator) {
    final int size = values.length;
    final double[] extremes = new double[size];
//    Arrays.fill(extremes, NOT_EXTREMUM);
    final List<Extremum> extrema = new ArrayList<>();

    // anchor first leg on first value if interpolation required
    int previousExtreme = ZERO;
    if (isInterpolate) {
      extremes[previousExtreme] = values[previousExtreme];
      extrema.add(new Extremum(previousExtreme, extremes[previousExtreme]));
    }

    // locate local extrema
    for (int today = ZERO; today < size; ++today) {
      final double value = values[today];
      boolean isExtreme = true;

      // look in the vicinity of both before and after (if possible)
      for (int offset = ONE; offset <= awayPoints; ++offset) {
        final int before = today - offset;
        final int after = today + offset;

        if (before < ZERO && after >= size) {
          break;               // fail fast if index out-of-bounds
        }
        else if (before < ZERO) {
          isExtreme &= comparator.isExtremum(value, values[after]);
        }
        else if (after >= size) {
          isExtreme &= comparator.isExtremum(value, values[before]);
        }
        else {
          isExtreme &= comparator.isExtremum(values[before], value, values[after]);
        }

        if (!isExtreme) {      // today's value is not an extremum
          today = after - ONE; // jump to new candidate
          break;
        }
      }

      if (isExtreme) {         // local extremum located
        extremes[today] = value;
        extrema.add(new Extremum(today, value));
        if (isInterpolate) {
          interpolate(previousExtreme,
                      extremes[previousExtreme],
                      today,
                      value,
                      extremes);
          previousExtreme = today;
        }
        logger.debug("New {} at ({}, {})", comparator, today, value);
        today += awayPoints;   // skip past immediate vicinity
      }
    }

    // anchor last leg on last value if interpolation required
    if (isInterpolate) {
      final int last = size - ONE;
      interpolate(previousExtreme,
                  extremes[previousExtreme],
                  last,
                  extremes[last] = values[last],
                  extremes);
    }

    return new ExtremumTimeSeries(comparator.seriesName, dates, extremes, extrema);
  }

  public static class Extremum {

    // Cartesian co-ordinates
    private final int    x;
    private final double y;

    public Extremum(final int x, final double y) {
      this.x = x;
      this.y = y;
    }

    public int x() {
      return x;
    }

    public double y() {
      return y;
    }

    @Override
    public String toString() {
      return String.format("%s (%d, %f)",
                           this.getClass().getSimpleName(),
                           x,
                           y);
    }

  }

  /**
   * Locate next extremum.
   *
   * @param values
   * @param current start search at next index
   * @return index of next extremum
   */
  public static final int nextExtremum(final double[] values, final int current) {
    int location = NOT_FOUND;
    for (int i = current + ONE; i < values.length; ++i) {
      if (isExtremum(values[i])) {
        location = i;
        break;
      }
    }
    return location;
  }

  /**
   * Locate previous extremum.
   *
   * @param values
   * @param current start search at previous index
   * @return index of previous extremum
   */
  public static final int previousExtremum(final double[] values, final int current) {
    int location = NOT_FOUND;
    for (int i = current - ONE; i >= ZERO; --i) {
      if (isExtremum(values[i])) {
        location = i;
        break;
      }
    }
    return location;
  }

  private static final boolean isExtremum(final double value) {
    return (value != NOT_EXTREMUM);
  }

}
