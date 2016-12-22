/**
 * SlowRSITest.java  0.1  22 December 2016 6:04:13 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SlowRSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SlowRSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;
  private static final int DEFAULT_EMA    = 6;

  public SlowRSITest() {
    super((DEFAULT_PERIOD - 1) + (DEFAULT_EMA - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SlowRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
