/**
 * ZeroLagDEMATest.java  0.1  8 July 2017 3:50:56 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.DEMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZeroLagDEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagDEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public ZeroLagDEMATest() {
    super((DEFAULT_PERIOD - 1) * 2 * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZeroLagDEMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("ZeroLagDEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        zldema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] zldema(final int period, final double... values) {
    final double[] dema1 = dema(period, values);
    final double[] dema2 = dema(period, dema1);

    final double[] zldema = new double[dema2.length];
    for (int i = 0, j = dema1.length - dema2.length; i < zldema.length; ++i, ++j) {
      zldema[i] = 2 * dema1[j] - dema2[i];
    }
    return zldema;
  }

}
