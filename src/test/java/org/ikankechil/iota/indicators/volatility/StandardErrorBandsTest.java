/**
 * StandardErrorBandsTest.java  v0.1  18 November 2016 6:03:44 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>StandardErrorBands</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StandardErrorBandsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD   = 21;
  private static final int SMOOTHING_PERIOD = 3;

  public StandardErrorBandsTest() {
    super((DEFAULT_PERIOD - 1) * 2 + SMOOTHING_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = StandardErrorBandsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
