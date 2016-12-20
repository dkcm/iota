/**
 * MovingAverageEnvelopes.java  0.1  20 December 2016 4:16:12 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Moving Average Envelopes
 *
 * <p>http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_envelopes<br>
 * http://www.investopedia.com/articles/trading/08/moving-average-envelope.asp<br>
 * http://www.stockfetcher.com/forums2/Indicators/Moving-Average-Envelopes/453<br>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MovingAverageEnvelopes extends AbstractIndicator {

  private final double        offset;

  private static final String UPPER_ENVELOPE  = "Moving Average Upper Envelope";
  private static final String MIDDLE_ENVELOPE = "Moving Average Middle Envelope";
  private static final String LOWER_ENVELOPE  = "Moving Average Lower Envelope";

  public MovingAverageEnvelopes() {
    this(TWENTY, TWO + HALF);
  }

  /**
   *
   *
   * @param period
   * @param offset envelope offset in percentages
   */
  public MovingAverageEnvelopes(final int period, final double offset) {
    super(period, (period - ONE));
    throwExceptionIfNegative(offset);

    this.offset = offset / HUNDRED_PERCENT;
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate((TimeSeries) ohlcv, start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    // Formula:
    // Upper Envelope: 20-day SMA + (20-day SMA x .025)
    // Lower Envelope: 20-day SMA - (20-day SMA x .025)

    throwExceptionIfShort(series);
    final int size = series.size();

    final double[] middleEnvelope = ema(series.values(), period);
    final double[] upperEnvelope = new double[middleEnvelope.length];
    final double[] lowerEnvelope = new double[middleEnvelope.length];
    for (int i = ZERO; i < middleEnvelope.length; ++i) {
      final double ma = middleEnvelope[i];
      final double envelope = ma * offset;
      upperEnvelope[i] = ma + envelope;
      lowerEnvelope[i] = ma - envelope;
    }

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(UPPER_ENVELOPE, dates, upperEnvelope),
                         new TimeSeries(MIDDLE_ENVELOPE, dates, middleEnvelope),
                         new TimeSeries(LOWER_ENVELOPE, dates, lowerEnvelope));
  }

}
