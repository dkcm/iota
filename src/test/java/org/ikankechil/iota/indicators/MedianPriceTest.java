/**
 * MedianPriceTest.java  v0.2 9 July 2015 5:22:32 PM
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
 * JUnit test for <code>MedianPrice</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MedianPriceTest extends AbstractIndicatorTest {

  public MedianPriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() {
    TEST_CLASS = MedianPriceTest.class;
  }

  @Ignore
  @Override
  public void cannotInstantiateWithNegativePeriod() { /* do nothing */ }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("MedianPrice",
                                        series.dates(),
                                        median(series)));
  }

  private static final double[] median(final OHLCVTimeSeries series) {
    final double[] med = new double[series.size()];
    for (int i = 0; i < med.length; ++i) {
      med[i] = (series.high(i) + series.low(i)) / 2;
    }
    return med;
  }

}
