/**
 * MACDTest.java  v0.3  11 December 2015 3:53:31 pm
 *
 * Copyright � 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MACD</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class MACDTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SLOW   = 26;
  private static final int DEFAULT_SIGNAL = 9;

  public MACDTest() {
    super((DEFAULT_SLOW - 1) + (DEFAULT_SIGNAL - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MACDTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new MACD(period, DEFAULT_SLOW, DEFAULT_SIGNAL);
  }

}
