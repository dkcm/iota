/**
 * VolatilityBandsTest.java  v0.1  24 September 2017 11:35:26 AM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VolatilityBands</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolatilityBandsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public VolatilityBandsTest() {
    super((DEFAULT_PERIOD - 1) * 7);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VolatilityBandsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
