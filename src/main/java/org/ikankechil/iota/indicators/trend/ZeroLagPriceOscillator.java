/**
 * ZeroLagPriceOscillator.java  v0.1  21 July 2015 10:36:12 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
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
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();
    final double[] closes = ohlcv.closes();

    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    // compute fast zero-lag EMA
    final double[] fastZeroLagEMA = new double[size - fast.lookback()];
    RetCode outcome = fast.compute(ZERO,
                                   size - ONE,
                                   closes,
                                   outBegIdx,
                                   outNBElement,
                                   fastZeroLagEMA);
    throwExceptionIfBad(outcome, ohlcv);

    // compute slow zero-lag EMA
    final double[] slowZeroLagEMA = new double[size - slow.lookback()];
    outcome = slow.compute(ZERO,
                           size - ONE,
                           closes,
                           outBegIdx,
                           outNBElement,
                           slowZeroLagEMA);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator
    final double[] zeroLagIndicator = new double[slowZeroLagEMA.length];
    for (int i = ZERO, j = fastZeroLagEMA.length - slowZeroLagEMA.length;
         i < zeroLagIndicator.length;
         ++i, ++j) {
      zeroLagIndicator[i] = compute(fastZeroLagEMA[j], slowZeroLagEMA[i]);
    }

    // compute indicator signal line - zero-lag EMA of indicator
    final double[] zeroLagIndicatorSignal = new double[size - lookback];
    outcome = signal.compute(ZERO,
                             zeroLagIndicator.length - ONE,
                             zeroLagIndicator,
                             outBegIdx,
                             outNBElement,
                             zeroLagIndicatorSignal);
    throwExceptionIfBad(outcome, ohlcv);

    // build output
    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(zeroLagIndicator,
                                                           signal.lookback(),
                                                           zeroLagIndicator.length)),
                         new TimeSeries(name + SIGNAL,
                                        dates,
                                        zeroLagIndicatorSignal));
  }

  abstract double compute(final double fastZeroLagEMA, final double slowZeroLagEMA);

}
