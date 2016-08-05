/**
 * WeightedClosePriceTest.java  v0.2  9 July 2015 5:57:27 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JUnit test for <code>WeightedClosePrice</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class WeightedClosePriceTest extends AbstractIndicatorTest {

  public WeightedClosePriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WeightedClosePriceTest.class;
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() { /* do nothing */ }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("WeightedClosePrice",
                                        series.dates(),
                                        weightedClose(series)));
  }

  private static final double[] weightedClose(final OHLCVTimeSeries series) {
    final double[] weightedClose = new double[series.size()];
    for (int i = 0; i < weightedClose.length; ++i) {
      weightedClose[i] = (series.high(i) + series.low(i) + (series.close(i) * 2)) / 4;
    }
    return weightedClose;
  }

}
