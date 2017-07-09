/**
 * ZeroLagEMATest.java  0.1  8 July 2017 2:52:56 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.EMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZeroLagEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public ZeroLagEMATest() {
    super((DEFAULT_PERIOD - 1) * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZeroLagEMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("ZeroLagEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        zlema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] zlema(final int period, final double... values) {
    final int emaLookback = period - 1;
    final double[] ema1 = ema(period, emaLookback, values);
    final double[] ema2 = ema(period, emaLookback, ema1);

    final double[] zlema = new double[ema2.length];
    for (int i = 0, j = ema1.length - ema2.length; i < zlema.length; ++i, ++j) {
      zlema[i] = 2 * ema1[j] - ema2[i];
    }
    return zlema;
  }

}
