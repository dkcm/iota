/**
 * KRITest.java  0.1  22 December 2016 5:06:25 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.trend.SMATest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KRI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KRITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public KRITest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KRITest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("KRI",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        kri(DEFAULT_PERIOD, series.closes())));
  }

  static final double[] kri(final int period, final double... values) {
    final double[] sma = SMATest.sma(period, values);

    // Formula:
    // KRI = (Price - SMA) / SMA * 100%
    final double[] kri = new double[sma.length];
    for (int i = 0, j = period - 1; i < kri.length; ++i, ++j) {
      kri[i] = (values[j] - sma[i]) / sma[i] * 100;
    }

    return kri;
  }

}
