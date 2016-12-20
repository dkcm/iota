/**
 * RMITest.java  0.1  20 December 2016 7:26:15 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RMI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD   = 20;
  private static final int DEFAULT_MOMENTUM = 5;

  public RMITest() {
    super(DEFAULT_PERIOD + DEFAULT_MOMENTUM - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RMITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
