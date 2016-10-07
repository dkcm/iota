/**
 * BOPTest.java  v0.1  6 October 2016 11:55:21 pm
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
import org.junit.BeforeClass;

/**
 * JUnit test for <code>BOP</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BOPTest extends AbstractIndicatorTest {

  public BOPTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = BOPTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("BOP",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        bop(series)));
  }

  private double[] bop(final OHLCVTimeSeries series) {
    final double[] bop = new double[series.size()];
    for (int i = 0; i < bop.length; ++i) {
      bop[i] = (series.close(i) - series.open(i)) / (series.high(i) - series.low(i));
    }
    return bop;
  }

}
