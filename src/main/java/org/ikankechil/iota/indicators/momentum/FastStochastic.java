/**
 * FastStochastic.java  v0.6  8 December 2014 8:49:06 PM
 *
 * Copyright � 2014-present Daniel Kuan.  All rights reserved.
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
 * <p>References:
 * <li>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full
 * <li>https://www.technicalindicators.net/indicators-technical-analysis/86-stochastic-oscillator-ssto-fsto<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.6
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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
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
    final String[] dates = new String[closes.length];
    final TimeSeries lowsTS = new TimeSeries(EMPTY, dates, lows);
    final TimeSeries highsTS = new TimeSeries(EMPTY, dates, highs);

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
    final TimeSeries mins = minPrice.generate(lowsTS).get(ZERO);
    final TimeSeries maxs = maxPrice.generate(highsTS).get(ZERO);

    return fastStochasticK(fastK, maxs, mins, closes);
  }

  public static final double[] fastStochasticK(final int fastK,
                                               final TimeSeries maxs,
                                               final TimeSeries mins,
                                               final double[] closes) {
    // compute fast K
    int c = fastK - ONE;
    final double[] fastKs = new double[closes.length - c];
    int i = ZERO;
    fastKs[i] = stochastic(maxs.value(i), mins.value(i), closes[c], ZERO);
    while (++i < fastKs.length) {
      fastKs[i] = stochastic(maxs.value(i), mins.value(i), closes[++c], fastKs[i - ONE]);
    }
    return fastKs;
  }

  private static double stochastic(final double max,
                                   final double min,
                                   final double close,
                                   final double previous) {
    return (max == min) ? previous // prevent divide-by-zero
                        : (close - min) / (max - min) * HUNDRED_PERCENT;
  }

}
