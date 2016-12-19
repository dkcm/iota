/**
 * IndicatorWithSignalLineAndHistogram.java  0.1  19 December 2016 2:35:01 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;

/**
 * Abstract superclass for <code>Indicator</code>s that have an EMA signal line
 * and histogram.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public abstract class IndicatorWithSignalLineAndHistogram extends IndicatorWithSignalLine {

  private final String        histogramName;

  private static final String HISTOGRAM = " Histogram";

  /**
   *
   *
   * @param signal signal line period
   * @param lookback sum of indicator and signal line lookbacks
   */
  public IndicatorWithSignalLineAndHistogram(final int signal, final int lookback) {
    this(ZERO, signal, lookback);
  }

  /**
   *
   *
   * @param period indicator period
   * @param signal signal line period
   * @param lookback sum of indicator and signal line lookbacks
   */
  public IndicatorWithSignalLineAndHistogram(final int period, final int signal, final int lookback) {
    super(period, signal, lookback);

    histogramName = name + HISTOGRAM;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    final List<TimeSeries> indicatorWithSignalLine = super.generate(ohlcv, start);

    final TimeSeries indicator = indicatorWithSignalLine.get(ZERO);
    final TimeSeries signalLine = indicatorWithSignalLine.get(ONE);

    // compute histogram = indicator - signal line
    final double[] histogram = difference(indicator.values(), signalLine.values());

    logger.info(GENERATED_FOR, name, ohlcv);
    return Arrays.asList(indicator,
                         signalLine,
                         new TimeSeries(histogramName,
                                        indicator.dates(),
                                        histogram));
  }

}
