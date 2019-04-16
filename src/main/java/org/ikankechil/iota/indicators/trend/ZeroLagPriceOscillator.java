/**
 * ZeroLagPriceOscillator.java  v0.2  21 July 2015 10:36:12 pm
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public abstract class ZeroLagPriceOscillator extends AbstractIndicator {

  private final ZeroLagEMA    fast;
  private final ZeroLagEMA    slow;
  private final ZeroLagEMA    signal;

  private static final String SIGNAL = " Signal";

  public ZeroLagPriceOscillator(final int fast, final int slow, final int signal) {
    super((Math.max(fast, slow) - ONE + signal - ONE) << ONE);

    this.fast = new ZeroLagEMA(fast);
    this.slow = new ZeroLagEMA(slow);
    this.signal = new ZeroLagEMA(signal);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    throwExceptionIfShort(series);

    // compute fast and slow zero-lag EMAs
    final double[] fastZeroLagEMA = generate(fast, series.values(), start);
    final double[] slowZeroLagEMA = generate(slow, series.values(), start);

    // compute indicator
    final double[] zeroLagIndicator = new double[slowZeroLagEMA.length];
    for (int i = ZERO, j = fastZeroLagEMA.length - slowZeroLagEMA.length;
         i < zeroLagIndicator.length;
         ++i, ++j) {
      zeroLagIndicator[i] = compute(fastZeroLagEMA[j], slowZeroLagEMA[i]);
    }

    // compute indicator signal line - zero-lag EMA of indicator
    final double[] zeroLagIndicatorSignal = generate(signal, zeroLagIndicator, ZERO);

    // build output
    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, series.size());

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(zeroLagIndicator,
                                                           signal.lookback(),
                                                           zeroLagIndicator.length)),
                         new TimeSeries(name + SIGNAL,
                                        dates,
                                        zeroLagIndicatorSignal));
  }

  private static double[] generate(final ZeroLagEMA indicator,
                                   final double[] values,
                                   final int start) {
    final double[] zeroLagEMA = new double[values.length - indicator.lookback()];
    indicator.compute(start,
                      values.length - ONE,
                      values,
                      new MInteger(),
                      new MInteger(),
                      zeroLagEMA);
    return zeroLagEMA;
  }

  abstract double compute(final double fastZeroLagEMA, final double slowZeroLagEMA);

}
