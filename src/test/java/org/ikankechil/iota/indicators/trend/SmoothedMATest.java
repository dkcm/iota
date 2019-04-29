/**
 * SmoothedMATest.java  v0.1  29 April 2019 11:25:35 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
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
 * JUnit test for {@code SmoothedMA}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SmoothedMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 9;

  public SmoothedMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SmoothedMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("SmoothedMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        smoothedMovingAverage(DEFAULT_PERIOD, series.closes())));
  }

  public static final double[] smoothedMovingAverage(final int period, final double... values) {
    final double[] smoothedMovingAverage = new double[values.length - (period - 1)];
    for (int i = 0; i < period; ++i) {
      smoothedMovingAverage[0] += values[i];
    }
    smoothedMovingAverage[0] /= period;
    for (int today = 1, yesterday = today - 1; today < smoothedMovingAverage.length; ++today) {
      smoothedMovingAverage[today] = (smoothedMovingAverage[yesterday] * (period - 1) + values[today + (period - 1)]) / period;
      yesterday = today;
    }
    return smoothedMovingAverage;
  }

}
