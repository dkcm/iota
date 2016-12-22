/**
 * WMATest.java  v0.2  8 October 2016 8:22:30 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
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
 * JUnit test for <code>WMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class WMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 6;

  public WMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("WMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        wma(DEFAULT_PERIOD, series.closes())));
  }

  static double[] wma(final int period, final double... values) {
    final double[] weights = weight(period);
    final double[] wmas = new double[values.length - period + 1];
    for (int i = 0; i < wmas.length; ++i) {
      double wma = 0;
      for (int j = 0; j < weights.length; ++j) {
        wma += values[i + j] * weights[j];
      }
      wmas[i] = wma;
    }
    return wmas;
  }

  private static final double[] weight(final int period) {
    final double denominator = period * (period + 1) / 2;
    final double[] weights = new double[period];
    for (int i = 0; i < weights.length; ++i) {
      weights[i] = (i + 1) / denominator;
    }
    return weights;
  }

}
