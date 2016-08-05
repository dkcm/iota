/**
 * RWI.java  v0.1 8 January 2015 7:51:26 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.RWI.SquareRoot.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 * Random Walk Index (RWI) by Michael Poulos
 * <p>
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V09/C02/OFTREND.PDF
 * ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V10/C09/ARETHER.PDF
 * http://edmond.mires.co/GES816/36-The%20Random%20Walk%20Revealed.pdf
 * http://user42.tuxfamily.org/chart/manual/Random-Walk-Index.html#Random-Walk-Index
 * http://www.metastock.com/customer/resources/formulas/formula.aspx?Id=48
 * http://fxcodebase.com/code/viewtopic.php?f=17&t=1166
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RWI extends AbstractIndicator {

  private static final int    MIN_LOOKBACK         = TWO;
  private static final int    DEFAULT_MAX_LOOKBACK = 64;

  private static final String RWI_HIGH             = "RWI High";
  private static final String RWI_LOW              = "RWI Low";

  private static final String TRUE_RANGE_GENERATED = "True range generated for: {} on {}";
  private static final String RWI_GENERATED        = "RWIs generated: index = {} (date = {}, high = {}, low = {})";

  /**
   * Short-term RWI: 2- to 7-day
   * Long-term RWI: 8- to 64-day
   *
   * @param maxLookback
   */
  public RWI(final int maxLookback) {
    super(maxLookback, maxLookback + TA_LIB.trueRangeLookback());

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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // RWI = Actual price move / Expected random walk
    // RWI High = MAX((High - Low(n)) / (ATR(n) * SQRT(n)))
    // RWI Low = MAX((High(n) - Low) / (ATR(n) * SQRT(n)))

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    final double[] highs = ohlcv.highs();
    final double[] lows = ohlcv.lows();
    final double[] closes = ohlcv.closes();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // compute True Range
    final double[] trueRange = new double[size - TA_LIB.trueRangeLookback()];
    final RetCode outcome = TA_LIB.trueRange(ZERO,
                                             size - ONE,
                                             highs,
                                             lows,
                                             closes,
                                             outBegIdx,
                                             outNBElement,
                                             trueRange);
    throwExceptionIfBad(outcome, ohlcv);
    logger.debug(TRUE_RANGE_GENERATED, name, ohlcv);

    return rwi(ohlcv, trueRange);
  }

  private final List<TimeSeries> rwi(final OHLCVTimeSeries ohlcv, final double[] trueRange) {
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

      logger.trace(RWI_GENERATED,
                   today,
                   ohlcv.date(today),
                   rwiHighs[i],
                   rwiLows[i]);
    }

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(RWI_HIGH, dates, rwiHighs),
                         new TimeSeries(RWI_LOW, dates, rwiLows));
  }

  private static final double ave(final double[] ds, final int from, final int to) {
    double sum = ds[from];
    for (int i = from + ONE; i < to; ++i) {
      sum += ds[i];
    }
    return sum / (to - from);
  }

}
