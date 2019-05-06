/**
 * Alligator.java  v0.1  21 April 2019 11:37:34 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.MedianPrice;

/**
 * Alligator by Bill Williams
 *
 * <p>References:
 * <li>https://www.investopedia.com/articles/trading/072115/exploring-williams-alligator-indicator.asp
 * <li>https://www.metatrader5.com/en/terminal/help/indicators/bw_indicators/alligator
 * <li>https://www.tradingview.com/ideas/alligator/
 * <li>http://forex-indicators.net/bill-williams/alligator-indicator
 * <li>https://library.tradingtechnologies.com/trade/chrt-ti-alligator.html<br>
 * <br>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class Alligator extends AbstractIndicator {

  private final int                teeth;
  private final int                lips;
  private final Indicator          blue;
  private final Indicator          red;
  private final Indicator          green;

  private static final MedianPrice MEDIAN_PRICE    = new MedianPrice();

  private static final String      ALLIGATOR_JAWS  = "Alligator Jaws";
  private static final String      ALLIGATOR_TEETH = "Alligator Teeth";
  private static final String      ALLIGATOR_LIPS  = "Alligator Lips";

  public Alligator() {
    this(THIRTEEN, EIGHT, FIVE);
  }

  public Alligator(final int jaws, final int teeth, final int lips) {
    super(jaws, jaws - ONE + teeth);
    throwExceptionIfNegative(jaws, teeth, lips);

    this.teeth = teeth;
    this.lips = lips;
    blue = new SmoothedMA(jaws);
    red = new SmoothedMA(teeth);
    green = new SmoothedMA(lips);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv, final int start) {
    return generate(MEDIAN_PRICE.generate(ohlcv, start).get(ZERO), start);
  }

  @Override
  public List<TimeSeries> generate(final TimeSeries series, final int start) {
    // Formula:
    // Alligator jaws (blue line) = Smoothed Moving Average (13) of Median, moved into the future by 8 bars
    // Alligator teeth (red line) = Smoothed Moving Average (8) of Median, moved into the future by 5 bars
    // Alligator lips (green line) = Smoothed Moving Average (5) of Median, moved into the future by 3 bars

    throwExceptionIfShort(series);
    final int size = series.size();

    final double[] blues = alligator(blue, series, start, teeth);
    final double[] reds = alligator(red, series, start, lips);
    final double[] greens = alligator(green, series, start, teeth - lips);

    logger.info(GENERATED_FOR, name, series);
    return Arrays.asList(new TimeSeries(ALLIGATOR_JAWS,
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           size),
                                        blues),
                         new TimeSeries(ALLIGATOR_TEETH,
                                        Arrays.copyOfRange(series.dates(),
                                                           size - reds.length,
                                                           size),
                                        reds),
                         new TimeSeries(ALLIGATOR_LIPS,
                                        Arrays.copyOfRange(series.dates(),
                                                           size - greens.length,
                                                           size),
                                        greens));
  }

  public static final double[] alligator(final Indicator indicator,
                                         final TimeSeries series,
                                         final int start,
                                         final int shift) {
    final double[] values = indicator.generate(series, start).get(ZERO).values();
    return Arrays.copyOf(values, values.length - shift);
  }

}
