/**
 * T3Test.java  v0.2  17 October 2016 7:30:39 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.GeneralisedDEMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>T3</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class T3Test extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 5;
  private static final int ITERATIONS     = 6;

  public T3Test() {
    super(ITERATIONS * (DEFAULT_PERIOD - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = T3Test.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("T3",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        t3(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] t3(final int period, final double... values) {
    // T3 = GD(GD(GD))
    return gdema(period,
                 gdema(period,
                       gdema(period, values)));
  }

}
