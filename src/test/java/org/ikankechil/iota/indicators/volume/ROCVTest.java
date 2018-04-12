/**
 * ROCVTest.java  v0.1  12 April 2018 10:31:18 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.ikankechil.iota.OHLCVTimeSeries;
import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ROCV</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ROCVTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 12;

  public ROCVTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ROCVTest.class;
  }

  @Override
  protected List<TimeSeries> generate(final OHLCVTimeSeries series) {
    return Arrays.asList(new TimeSeries("ROCV",
                                        Arrays.copyOfRange(series.dates(),
                                                           lookback,
                                                           series.size()),
                                        rocv(DEFAULT_PERIOD, series.volumes())));
  }

  private static double[] rocv(final int period, final long... volumes) {
    final double[] rocv = new double[volumes.length - period];
    for (int i = 0; i < rocv.length; ++i) {
      rocv[i] = ((volumes[i + period] - volumes[i]) / (double) volumes[i]) * 100;
    }
    return rocv;
  }

}
