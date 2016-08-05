/**
 * ZigZagTrendTest.java  v0.2 16 December 2015 9:31:57 AM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZigZagTrend</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ZigZagTrendTest extends AbstractIndicatorTest {

  public ZigZagTrendTest() {
    super(2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZigZagTrendTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new ZigZagTrend(5); // TODO remove
  }

}
