/**
 * FastStochastic.java  v0.2  8 December 2014 8:49:06 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Fast Stochastic Oscillator by George C. Lane
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:stochastic_oscillator_fast_slow_and_full
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class FastStochastic extends AbstractIndicator {

  private final int fastK;
  private final int fastD;

  private static final String K = "Fast Stochastic %K";
  private static final String D = "Fast Stochastic %D";

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

//    final int size = ohlcv.size();
//
//    final MInteger outBegIdx = new MInteger();
//    final MInteger outNBElement = new MInteger();
//
//    final double[] outFastK = new double[size - lookback];
//    final double[] outFastD = new double[outFastK.length];
//
//    final RetCode outcome = TA_LIB.stochF(ZERO,
//                                          size - ONE,
//                                          ohlcv.highs(),
//                                          ohlcv.lows(),
//                                          ohlcv.closes(),
//                                          fastK,
//                                          fastD,
//                                          MAType.Sma,
//                                          outBegIdx,
//                                          outNBElement,
//                                          outFastK,
//                                          outFastD);
//    throwExceptionIfBad(outcome, ohlcv);
//
//    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);
//
//    logger.info(GENERATED_FOR, name, ohlcv);
//    return Arrays.asList(new TimeSeries(K, dates, outFastK),
//                         new TimeSeries(D, dates, outFastD));
  }

  public static final double[] fastStochasticK(final int fastK, final OHLCVTimeSeries ohlcv) {
    int c = fastK - ONE;
    final double[] fastKs = new double[ohlcv.size() - c];
    for (int i = ZERO, j = c + ONE; i < fastKs.length; ++i, ++j, ++c) {
      final double max = max(ohlcv.highs(), i, j);
      final double min = min(ohlcv.lows(), i, j);
      fastKs[i] = (ohlcv.close(c) - min) / (max - min) * HUNDRED_PERCENT;
    }
    return fastKs;
  }

}
