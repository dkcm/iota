/**
 * ZeroLagTEMATest.java  0.1  8 July 2017 10:53:13 AM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.TEMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZeroLagTEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagTEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public ZeroLagTEMATest() {
    super((DEFAULT_PERIOD - 1) * 3 * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZeroLagTEMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("ZeroLagTEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        zltema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] zltema(final int period, final double... values) {
    final double[] tema1 = tema(period, values);
    final double[] tema2 = tema(period, tema1);

    final double[] zltema = new double[tema2.length];
    for (int i = 0, j = tema1.length - tema2.length; i < zltema.length; ++i, ++j) {
      zltema[i] = 2 * tema1[j] - tema2[i];
    }
    return zltema;
  }

}
