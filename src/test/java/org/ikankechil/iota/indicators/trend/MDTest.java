/**
 * MDTest.java  v0.1  15 January 2017 11:52:34 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MD</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MDTest extends AbstractIndicatorTest {

  private static final int DEFAULT_N     = 10;
  private static final int DEFAULT_POWER = 4;

  public MDTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MDTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("MD",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        md(series.closes())));
  }

  private static final double[] md(final double... values) {
    final double[] md = new double[values.length];
    double pmd = md[0] = values[0];
    for (int i = 1; i < md.length; ++i) {
      pmd = md[i] = pmd + (values[i] - pmd) / (DEFAULT_N * Math.pow((values[i] / pmd), DEFAULT_POWER));
    }
    return md;
  }

}
