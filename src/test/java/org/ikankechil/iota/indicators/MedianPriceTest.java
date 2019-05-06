/**
 * MedianPriceTest.java  v0.3  9 July 2015 5:22:32 PM
 *
 * Copyright © 2015-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code MedianPrice}.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MedianPriceTest extends AbstractIndicatorTest {

  public MedianPriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    TEST_CLASS = MedianPriceTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("MedianPrice",
                                        series.dates(),
                                        median(series)));
  }

  public static final double[] median(final OHLCVTimeSeries series) {
    final double[] median = new double[series.size()];
    for (int i = 0; i < median.length; ++i) {
      median[i] = (series.high(i) + series.low(i)) / 2;
    }
    return median;
  }

}
