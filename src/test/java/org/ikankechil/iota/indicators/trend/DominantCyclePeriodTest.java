/**
 * DominantCyclePeriodTest.java  v0.1  5 August 2016 12:26:32 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;
import java.util.List;

import org.ikankechil.iota.TimeSeries;
import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DominantCyclePeriod</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DominantCyclePeriodTest extends AbstractIndicatorTest {

  public static void main(final String... arguments) {
    final double omega = 2 * Math.PI;

    final DominantCyclePeriod dcp = new DominantCyclePeriod();
    final int size = dcp.lookback() * 10;
    final TimeSeries series = new TimeSeries("DominantCyclePeriodTest", size);
    for (int i = 0, t = 0; i < series.size(); ++i) {
      series.set(String.valueOf(i), Math.sin(omega * t), i);
    }

    final List<TimeSeries> indicator = dcp.generate(series);

  }

  public DominantCyclePeriodTest() {
    super(32);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
//    TEST_CLASS = DominantCyclePeriodTest.class;
//    AbstractIndicatorTest.setUpBeforeClass();
  }

}
