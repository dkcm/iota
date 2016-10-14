/**
 * AveragePriceTest.java  v0.2  9 July 2015 5:05:52 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JUnit test for <code>AveragePrice</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AveragePriceTest extends AbstractIndicatorTest {

  public AveragePriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    TEST_CLASS = AveragePriceTest.class;
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() { /* do nothing */ }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("AveragePrice",
                                        series.dates(),
                                        average(series)));
  }

  private static final double[] average(final OHLCVTimeSeries series) {
    final double[] average = new double[series.size()];
    for (int i = 0; i < average.length; ++i) {
      average[i] = (series.open(i) + series.high(i) + series.low(i) + series.close(i)) / 4;
    }
    return average;
  }

}
