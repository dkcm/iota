/**
 * TypicalPriceTest.java  v0.2  9 July 2015 5:38:51 PM
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
 * JUnit test for <code>TypicalPrice</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TypicalPriceTest extends AbstractIndicatorTest {

  public TypicalPriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TypicalPriceTest.class;
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() { /* do nothing */ }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("TypicalPrice",
                                        series.dates(),
                                        typical(series)));
  }

  private static final double[] typical(final OHLCVTimeSeries series) {
    final double[] med = new double[series.size()];
    for (int i = 0; i < med.length; ++i) {
      med[i] = (series.high(i) + series.low(i) + series.close(i)) / 3;
    }
    return med;
  }

}
