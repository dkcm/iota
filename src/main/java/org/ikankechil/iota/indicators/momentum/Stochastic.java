/**
 * Stochastic.java  v0.3  4 December 2014 2:05:03 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.ikankechil.iota.indicators.momentum.FastStochastic.*;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Stochastic Oscillator by George C. Lane
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full<br>
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class Stochastic extends AbstractIndicator {

  private final int           fastK;
  private final int           slowK;
  private final int           slowD;

  private static final String K = "Stochastic %K";
  private static final String D = "Stochastic %D";

  public Stochastic() {
    this(FOURTEEN, THREE, THREE);
  }

  /**
   *
   *
   * @param fastK lookback period for computing fast %K
   * @param slowK smoothing factor for fast %K (to get slow %K)
   * @param slowD number of periods for the %D moving average
   */
  public Stochastic(final int fastK, final int slowK, final int slowD) {
    super(fastK + slowK + slowD - THREE);
    throwExceptionIfNegative(fastK, slowK, slowD);

    this.fastK = fastK;
    this.slowK = slowK;
    this.slowD = slowD;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    // Formula:
    // Full %K = Fast %K smoothed with X-period SMA
    // Full %D = X-period SMA of Full %K

    throwExceptionIfShort(ohlcv);

    // compute fast %K
    final double[] fastKs = fastStochasticK(fastK, ohlcv);
    // smooth fast %K = fast %D = slow %K
    final double[] slowKs = sma(fastKs, slowK);
    // smooth slow %K = slow %D
    final double[] slowDs = sma(slowKs, slowD);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, ohlcv.size());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(K,
                                        dates,
                                        Arrays.copyOfRange(slowKs,
                                                           slowD - ONE,
                                                           slowKs.length)),
                         new TimeSeries(D,
                                        dates,
                                        slowDs));
  }

}
