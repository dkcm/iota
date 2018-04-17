/**
 * RocketRSITest.java  v0.1  15 April 2018 1:41:37 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RocketRSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RocketRSITest extends AbstractIndicatorTest {

  public RocketRSITest() {
    super(19);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RocketRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
