/**
 * MACDTest.java  v0.2  11 December 2015 3:53:31 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MACD</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MACDTest extends AbstractIndicatorTest {

  public MACDTest() {
    super(33);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MACDTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new MACD(period, 26, 9);
  }

}
