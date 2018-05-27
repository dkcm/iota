/**
 * WildersSmoothingTest.java  v0.1  23 May 2018 11:14:22 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
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
 * JUnit test for <code>WildersSmoothing</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WildersSmoothingTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public WildersSmoothingTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WildersSmoothingTest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("WildersSmoothing",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        EMATest.ema(DEFAULT_PERIOD, 1 / (double) DEFAULT_PERIOD, lookback, series.closes())));
  }

}
