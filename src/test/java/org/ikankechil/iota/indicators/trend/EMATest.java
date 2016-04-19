/**
 * EMATest.java v0.2 10 January 2015 2:14:02 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>EMA</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class EMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 6;

  public EMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = EMATest.class;
  }

  @Override
  public Indicator newInstance()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("EMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        ema(DEFAULT_PERIOD, lookback, series.closes())));
  }

  static final double[] ema(final int period, final int lb, final double... values) {
    // EMA = alpha * Price + (1 – alpha) * EMA[1]
    //     = EMA[1] + alpha * (Price – EMA[1])
    // where
    // EMA[1] = value of the EMA one bar ago.
    // alpha = 2 / (period + 1)

    final double[] ema = new double[values.length - lb];

    final double alpha = 2.0 / (period + 1);
    final double one_alpha = 1 - alpha;

    for (int i = 0; i < period; ++i) {
      ema[0] += values[i];
    }
    ema[0] /= period;

    for (int today = 1, yesterday = today - 1; today < ema.length; ++today) {
      ema[today] = alpha * values[today + lb] + one_alpha * ema[yesterday];
      yesterday = today;
    }

    return ema;
  }

}
