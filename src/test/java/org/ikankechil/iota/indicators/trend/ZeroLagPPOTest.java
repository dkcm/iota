/**
 * ZeroLagPPOTest.java  0.1  8 July 2017 11:01:17 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZeroLagPPO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZeroLagPPOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SLOW   = 21;
  private static final int DEFAULT_SIGNAL = 8;

  public ZeroLagPPOTest() {
    super(((DEFAULT_SLOW - 1) + (DEFAULT_SIGNAL - 1)) * 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZeroLagPPOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new ZeroLagPPO(period, DEFAULT_SLOW, DEFAULT_SIGNAL);
  }

}
