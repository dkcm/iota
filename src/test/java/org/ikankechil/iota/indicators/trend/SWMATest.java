/**
 * SWMATest.java  v0.1  10 January 2017 11:59:20 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import static java.lang.Math.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SWMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SWMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 30;

  public SWMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SWMATest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("SWMA",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        swma(DEFAULT_PERIOD, series.closes())));
  }

  public static final double[] swma(final int period, final double... values) {
    // compute weights
    double sum = 0;
    final double[] sineWeights = new double[period];
    for (int i = 0; i < sineWeights.length; ) {
      sum += sineWeights[i] = sin(PI * ++i / (period + 1));
    }

    // compute SWMA
    final double[] swma = new double[values.length - (period - 1)];
    for (int i = 0; i < swma.length; ++i) {
      for (int j = 0, k = i; j < sineWeights.length; ++j, ++k) {
        swma[i] += sineWeights[j] * values[k];
      }
      swma[i] /= sum;
    }

    return swma;
  }

}
