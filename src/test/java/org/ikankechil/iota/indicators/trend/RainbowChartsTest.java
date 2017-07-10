/**
 * RainbowChartsTest.java  v0.3  30 November 2015 1:28:14 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.SMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RainbowCharts</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class RainbowChartsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 2;

  public RainbowChartsTest() {
    super((DEFAULT_PERIOD - 1) * 10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RainbowChartsTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    final double[] sma1 = sma(DEFAULT_PERIOD, series.closes());
    final double[] sma2 = sma(DEFAULT_PERIOD, sma1);
    final double[] sma3 = sma(DEFAULT_PERIOD, sma2);
    final double[] sma4 = sma(DEFAULT_PERIOD, sma3);
    final double[] sma5 = sma(DEFAULT_PERIOD, sma4);
    final double[] sma6 = sma(DEFAULT_PERIOD, sma5);
    final double[] sma7 = sma(DEFAULT_PERIOD, sma6);
    final double[] sma8 = sma(DEFAULT_PERIOD, sma7);
    final double[] sma9 = sma(DEFAULT_PERIOD, sma8);
    final double[] sma10 = sma(DEFAULT_PERIOD, sma9);

    final String[] dates = Arrays.copyOfRange(series.dates(), lookback, series.size());

    final String name = "RainbowCharts";
    return Arrays.asList(new TimeSeries(name, dates, Arrays.copyOfRange(sma1, sma1.length - sma10.length, sma1.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma2, sma2.length - sma10.length, sma2.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma3, sma3.length - sma10.length, sma3.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma4, sma4.length - sma10.length, sma4.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma5, sma5.length - sma10.length, sma5.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma6, sma6.length - sma10.length, sma6.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma7, sma7.length - sma10.length, sma7.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma8, sma8.length - sma10.length, sma8.length)),
                         new TimeSeries(name, dates, Arrays.copyOfRange(sma9, sma9.length - sma10.length, sma9.length)),
                         new TimeSeries(name, dates, sma10));
  }

}
