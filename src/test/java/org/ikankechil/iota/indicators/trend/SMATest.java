/**
 * SMATest.java  v0.3  10 January 2015 1:51:14 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class SMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 9;

  public SMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("SMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        sma(DEFAULT_PERIOD, series.closes())));
  }

  public static final double[] sma(final int period, final double... values) {
    final double[] sma = new double[values.length - (period - 1)];
    for (int i = 0; i < period; ++i) {
      sma[0] += values[i];
    }
    sma[0] /= period;
    for (int today = 1, yesterday = today - 1; today < sma.length; ++today) {
      // drop earliest entry
      sma[today] = sma[yesterday] + ((values[today + (period - 1)] - values[yesterday]) / period);
      yesterday = today;
    }
    return sma;
  }

}
