/**
 * DEMATest.java  v0.2  17 October 2016 10:00:16 pm
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
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
 * JUnit test for <code>DEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public DEMATest() {
    super((DEFAULT_PERIOD - 1) * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DEMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("DEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        dema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] dema(final int period, final double... values) {
    final int emaLookback = period - 1;
    final double[] ema1 = EMATest.ema(period, emaLookback, values);
    final double[] ema2 = EMATest.ema(period, emaLookback, ema1);
    final double[] dema = new double[ema2.length];
    for (int i = 0, j = emaLookback; i < dema.length; ++i, ++j) {
      dema[i] = (2 * ema1[j]) - ema2[i];
    }
    return dema;
  }

}
