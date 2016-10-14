/**
 * RainbowCharts.java  v0.2  1 March 2015 5:58:00 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicator;

/**
 * Rainbow Charts by Mel Widner
 *
 * <p>ftp://80.240.216.180/Transmission/%D0%A4%D0%B0%D0%B9%D0%BB%D1%8B/S&C%20on%20DVD%2011.26/VOLUMES/V15/C07/RAINBOW.pdf<br>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RainbowCharts extends AbstractIndicator {

  public RainbowCharts() {
    this(TWO);
  }

  public RainbowCharts(final int period) {
    super(period, TA_LIB.smaLookback(period) * NINE);
  }

  @Override
  public List<TimeSeries> generate(final OHLCVTimeSeries ohlcv) {
    throwExceptionIfShort(ohlcv);

    final double[] ma0 = sma(ohlcv.closes(), period);
    final double[] ma1 = sma(ma0, period);
    final double[] ma2 = sma(ma1, period);
    final double[] ma3 = sma(ma2, period);
    final double[] ma4 = sma(ma3, period);
    final double[] ma5 = sma(ma4, period);
    final double[] ma6 = sma(ma5, period);
    final double[] ma7 = sma(ma6, period);
    final double[] ma8 = sma(ma7, period);
    final double[] ma9 = sma(ma8, period);

    final int size = ohlcv.size();
    final String[] dates = Arrays.copyOfRange(ohlcv.dates(), lookback, size);

    logger.info(GENERATED_FOR, name, ohlcv);

    // MAs have different lengths
    final int length = ma9.length;
    return Arrays.asList(new TimeSeries(name, dates, Arrays.copyOfRange(ma0, ma0.length - length, ma0.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma1, ma1.length - length, ma1.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma2, ma2.length - length, ma2.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma3, ma3.length - length, ma3.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma4, ma4.length - length, ma4.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma5, ma5.length - length, ma5.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma6, ma6.length - length, ma6.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma7, ma7.length - length, ma7.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(ma8, ma8.length - length, ma8.length)),
                         new TimeSeries(name, dates, ma9));
  }

}
