/**
 * MACD.java  v0.2  27 November 2014 1:06:03 am
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Moving Average Convergence/Divergence (MACD) by Gerald Appel
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_convergence_divergence_macd
 * http://www.investopedia.com/terms/m/macd.asp
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MACD extends AbstractIndicator {

  private final int           fast;
  private final int           slow;
  private final int           signal;

  private static final String MACD_SIGNAL    = "MACD Signal";
  private static final String MACD_HISTOGRAM = "MACD Histogram";

  public MACD() {
    this(TWELVE, TWENTY_SIX, NINE);
  }

  public MACD(final int fast, final int slow, final int signal) {
    super(Math.max(fast, slow) + signal - TWO);
    throwExceptionIfNegative(fast, slow, signal);

    this.fast = fast;
    this.slow = slow;
    this.signal = signal;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    // Formula:
    // MACD Line: (12-day EMA - 26-day EMA)
    // Signal Line: 9-day EMA of MACD Line
    // MACD Histogram: MACD Line - Signal Line

    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    // compute fast and slow EMAs
    final double[] fastEMAs = ema(closes, fast);
    final double[] slowEMAs = ema(closes, slow);

    // compute MACD
    final double[] macd = difference(fastEMAs, slowEMAs);

    // compute MACD signal
    final double[] macdSignal = ema(macd, signal);

    // compute MACD histogram
    final double[] macdHistogram = difference(macd, macdSignal);

    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(macd,
                                                           signal - ONE, // signal lookback
                                                           macd.length)),
                         new TimeSeries(MACD_SIGNAL,
                                        dates,
                                        macdSignal),
                         new TimeSeries(MACD_HISTOGRAM,
                                        dates,
                                        macdHistogram));
  }

  private static final double[] difference(final double[] finals, final double[] initials) {
    final double[] differences = new double[initials.length];
    for (int i = ZERO, j = finals.length - initials.length; i < differences.length; ++i, ++j) {
      differences[i] = finals[j] - initials[i];
    }
    return differences;
  }

}
