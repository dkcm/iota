/**
 * TRTest.java v0.2 9 July 2015 6:14:43 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JUnit test for <code>TR</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TRTest extends AbstractIndicatorTest {

  public TRTest() {
    super(1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = TRTest.class;
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() { /* do nothing */ }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("TR",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        trueRange(series)));
  }

  private static final double[] trueRange(final OHLCVTimeSeries series) {
    final double[] trueRange = new double[series.size() - 1];
    for (int i = 0, j = i + 1; i < trueRange.length; ++i, ++j) {
      final double high = series.high(j);
      final double low = series.low(j);
      final double close = series.close(i);
      trueRange[i] = Math.max(high - low,
                     Math.max(Math.abs(close - high),
                              Math.abs(close - low)));
    }
    return trueRange;
  }

}
