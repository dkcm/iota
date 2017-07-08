/**
 * TEMATest.java  v0.1  24 October 2016 10:53:23 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
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
 * JUnit test for <code>TEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TEMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public TEMATest() {
    super((DEFAULT_PERIOD - 1) * 3);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TEMATest.class;
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("TEMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        tema(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] tema(final int period, final double... values) {
    final int emaLookback = period - 1;
    final double[] ema1 = EMATest.ema(period, emaLookback, values);
    final double[] ema2 = EMATest.ema(period, emaLookback, ema1);
    final double[] tema = EMATest.ema(period, emaLookback, ema2);
    for (int i = 0, j = emaLookback, k = emaLookback * 2;
         i < tema.length;
         ++i, ++j, ++k) {
      tema[i] += 3 * (ema1[k] - ema2[j]);
    }
    return tema;
  }

}
