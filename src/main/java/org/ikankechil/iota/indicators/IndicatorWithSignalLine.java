/**
 * IndicatorWithSignalLine.java  v0.2  19 December 2014 6:14:24 PM
 *
 * Copyright © 2014-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public abstract class IndicatorWithSignalLine extends AbstractIndicator {

  private final int           signal;
  private final int           signalLookback;
  protected final String      signalName;

  private static final String SIGNAL = " Signal";

  /**
   * @param signal signal line period
   * @param lookback sum of indicator and signal line lookbacks
   */
  public IndicatorWithSignalLine(final int signal, final int lookback) {
    super(lookback);
    throwExceptionIfNegative(signal);

    this.signal = signal;
    signalLookback = signal - ONE;
    signalName = name + SIGNAL;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);
    final int size = ohlcv.size();

    // compute indicator
    final MInteger outBegIdx = new MInteger();
    final MInteger outNBElement = new MInteger();

    final double[] indicator = new double[(size - lookback) + signalLookback];

    final RetCode outcome = compute(ZERO,
                                    size - ONE,
                                    ohlcv,
                                    outBegIdx,
                                    outNBElement,
                                    indicator);
    throwExceptionIfBad(outcome, ohlcv);

    // compute indicator signal line
    final double[] indicatorSignal = ema(indicator, signal); // = (size - lookback)

    // build output
    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(new TimeSeries(name,
                                        dates,
                                        Arrays.copyOfRange(indicator,  // copy only what is common to indicator and indicator signal line
                                                           signalLookback,
                                                           indicator.length)),
                         new TimeSeries(signalName,
                                        dates,
                                        indicatorSignal));
  }

}
