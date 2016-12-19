/**
 * VWMACDTest.java  0.1  19 December 2016 4:32:51 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VWMACD</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VWMACDTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SLOW   = 26;
  private static final int DEFAULT_SIGNAL = 9;

  public VWMACDTest() {
    super((DEFAULT_SLOW - 1) + (DEFAULT_SIGNAL - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VWMACDTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new VWMACD(period, DEFAULT_SLOW, DEFAULT_SIGNAL);
  }

}
