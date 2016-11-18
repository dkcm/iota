/**
 * FastStochastic.java  v0.4  8 December 2014 8:49:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.MaximumPrice;
import org.ikankechil.iota.indicators.MinimumPrice;

/**
 * Fast Stochastic Oscillator by George C. Lane
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class FastStochastic extends AbstractIndicator {

  private final int                               fastK;
  private final int                               fastD;

  private static final Map<Integer, MinimumPrice> MINS = new HashMap<>();
  private static final Map<Integer, MaximumPrice> MAXS = new HashMap<>();

  private static final String                     K    = "Fast Stochastic %K";
  private static final String                     D    = "Fast Stochastic %D";

  public FastStochastic() {
    this(FOURTEEN, THREE);
  }

  public FastStochastic(final int fastK, final int fastD) {
    super(fastK + fastD - TWO);
    throwExceptionIfNegative(fastK, fastD);

    this.fastK = fastK;
    this.fastD = fastD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // Fast Stochastic
    // %K = (Current Close - Lowest Low)/(Highest High - Lowest Low) * 100
    // %D = n-day SMA of %K
    //
    // Lowest Low = lowest low for the lookback period
    // Highest High = highest high for the lookback period
    // %K is multiplied by 100 to move the decimal point two places

    throwExceptionIfShort(ohlcv);

    // compute fast %K
    final double[] fastKs = fastStochasticK(fastK, ohlcv);
    // smooth fast %K = fast %D
    final double[] fastDs = sma(fastKs, fastD);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(K,
                                        dates,
                                        Arrays.copyOfRange(fastKs,
                                                           fastD - ONE,
                                                           fastKs.length)),
                         new TimeSeries(D,
                                        dates,
                                        fastDs));
  }

  public static final double[] fastStochasticK(final int fastK, final OHLCVTimeSeries ohlcv) {
    return fastStochasticK(fastK, ohlcv.highs(), ohlcv.lows(), ohlcv.closes());
  }

  public static final double[] fastStochasticK(final int fastK,
                                               final double[] highs,
                                               final double[] lows,
                                               final double[] closes) {
    // copy highs and lows
    final int size = closes.length;
    final TimeSeries lowsTS = new TimeSeries(EMPTY, size);
    final TimeSeries highsTS = new TimeSeries(EMPTY, size);
    System.arraycopy(lows, ZERO, lowsTS.values(), ZERO, size);
    System.arraycopy(highs, ZERO, highsTS.values(), ZERO, size);

    // retrieve
    final MinimumPrice minPrice;
    final MaximumPrice maxPrice;
    if (MINS.containsKey(fastK)) {
      minPrice = MINS.get(fastK);
      maxPrice = MAXS.get(fastK);
    }
    else {
      MINS.put(fastK, minPrice = new MinimumPrice(fastK));
      MAXS.put(fastK, maxPrice = new MaximumPrice(fastK));
    }

    // generate min and max
    final double[] mins = minPrice.generate(lowsTS).get(ZERO).values();
    final double[] maxs = maxPrice.generate(highsTS).get(ZERO).values();

    // compute fast K
    int c = fastK - ONE;
    final double[] fastKs = new double[size - c];
    for (int i = ZERO; i < fastKs.length; ++i, ++c) {
      final double max = maxs[i];
      final double min = mins[i];
      fastKs[i] = (closes[c] - min) / (max - min) * HUNDRED_PERCENT;
    }
    return fastKs;
  }

}
