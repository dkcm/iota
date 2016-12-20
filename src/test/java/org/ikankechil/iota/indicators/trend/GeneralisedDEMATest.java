/**
 * GeneralisedDEMATest.java  v0.1  17 October 2016 9:53:58 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static org.ikankechil.iota.indicators.trend.EMATest.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>GeneralisedDEMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GeneralisedDEMATest extends AbstractIndicatorTest {

  private static final int    DEFAULT_PERIOD = 7;
  private static final double VOLUME_FACTOR  = 0.7;

  public GeneralisedDEMATest() {
    super((DEFAULT_PERIOD - 1) * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GeneralisedDEMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("GeneralisedDEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        gdema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] gdema(final int period, final double... values) {
    // GD(n,v) = EMA(n)*(1+v) - EMA(EMA(n))*v

    final int lookback = period - 1;
    final double[] ema = ema(period, lookback, values);
    final double[] ema2 = ema(period, lookback, ema);

    final double[] gdema = new double[values.length - lookback * 2];
    for (int i = 0, j = ema.length - ema2.length; i < gdema.length; ++i, ++j) {
      gdema[i] = ema[j] * (1 + VOLUME_FACTOR) - ema2[i] * VOLUME_FACTOR;
    }

    return gdema;
  }

}
