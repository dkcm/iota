/**
 * RainbowChartsTest.java  v0.2 30 November 2015 1:28:14 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RainbowCharts</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RainbowChartsTest extends AbstractIndicatorTest {

  public RainbowChartsTest() {
    super(9);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RainbowChartsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
