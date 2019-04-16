/**
 * RWI.java  v0.3  8 January 2015 7:51:26 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.RWI.SquareRoot.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.volatility.TR;

/**
 * Random Walk Index (RWI) by Michael Poulos
 *
 * <p>References:
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V09/C02/OFTREND.PDF
 * <li>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V10/C09/ARETHER.PDF
 * <li>http://edmond.mires.co/GES816/36-The%20Random%20Walk%20Revealed.pdf
 * <li>http://user42.tuxfamily.org/chart/manual/Random-Walk-Index.html#Random-Walk-Index
 * <li>http://www.metastock.com/customer/resources/formulas/formula.aspx?Id=48
 * <li>http://fxcodebase.com/code/viewtopic.php?f=17&t=1166<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class RWI extends AbstractIndicator {

  private static final Indicator TR                   = new TR();

  private static final int       MIN_LOOKBACK         = TWO;
  private static final int       DEFAULT_MAX_LOOKBACK = 64;

  private static final String    RWI_HIGH             = "RWI High";
  private static final String    RWI_LOW              = "RWI Low";

  /**
   * Short-term RWI: 2- to 7-day
   * Long-term RWI: 8- to 64-day
   *
   * @param maxLookback
   */
  public RWI(final int maxLookback) {
    super(maxLookback, maxLookback + TR.lookback());

    sqrt(maxLookback);  // initialise, if required when maxLookback > DEFAULT_MAX_LOOKBACK
  }

  static class SquareRoot {

    private static double[] sqrt;

    static {
      sqrt = new double[DEFAULT_MAX_LOOKBACK + ONE];
      sqrt[ONE] = ONE;
      for (int n = MIN_LOOKBACK; n < sqrt.length; ++n) {
        sqrt[n] = Math.sqrt(n);
      }
      logger.debug("sqrt initialised up to sqrt({})", DEFAULT_MAX_LOOKBACK);
    }

    public static double sqrt(final int n) {
      if ((n + ONE) > sqrt.length) {
        final int oldLength = sqrt.length;
        sqrt = Arrays.copyOf(sqrt, n + ONE);
        for (int i = oldLength; i < sqrt.length; ++i) {
          sqrt[i] = Math.sqrt(i);
        }

        logger.info("New sqrt length: {} -> {}", oldLength, sqrt.length);
      }
      return sqrt[n];
    }

  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // RWI = Actual price move / Expected random walk
    // RWI High = MAX((High - Low(n)) / (ATR(n) * SQRT(n)))
    // RWI Low = MAX((High(n) - Low) / (ATR(n) * SQRT(n)))

    throwExceptionIfShort(ohlcv);

    // compute True Range
    final double[] trueRange = TR.generate(ohlcv, start).get(ZERO).values();

    final int size = ohlcv.size();
    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();

    // compute indicator
    final double[] rwiHighs = new double[size - lookback];
    final double[] rwiLows = new double[rwiHighs.length];

    final double[] rwiHighsToday = new double[period - ONE];
    final double[] rwiLowsToday = new double[rwiHighsToday.length];

    for (int i = ZERO, today = lookback; i < rwiHighs.length; ++i, ++today) {
      // compute today's RWIs
      for (int j = ZERO, n = MIN_LOOKBACK; j < rwiHighsToday.length; ++j, ++n) {
        final int nDaysAgo = today - n;
        final double randomWalk = ave(trueRange, nDaysAgo, today) * sqrt(n);

        rwiHighsToday[j] = (highs[today] - lows[nDaysAgo]) / randomWalk;
        rwiLowsToday[j] = (highs[nDaysAgo] - lows[today]) / randomWalk;
      }

      rwiHighs[i] = max(rwiHighsToday);
      rwiLows[i] = max(rwiLowsToday);
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(RWI_HIGH, dates, rwiHighs),
                         new TimeSeries(RWI_LOW, dates, rwiLows));
  }

  private static double ave(final double[] ds, final int from, final int to) {
    double sum = ds[from];
    for (int i = from + ONE; i < to; ++i) {
      sum += ds[i];
    }
    return sum / (to - from);
  }

}
