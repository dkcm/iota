/**
 * AccelerationBandsTest.java  v0.1  17 November 2016 3:54:31 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AccelerationBands</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AccelerationBandsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public AccelerationBandsTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AccelerationBandsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
