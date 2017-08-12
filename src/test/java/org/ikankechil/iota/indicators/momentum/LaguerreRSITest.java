/**
 * LaguerreRSITest.java  v0.2  18 September 2016 11:49:06 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LaguerreRSI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class LaguerreRSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 4;

  public LaguerreRSITest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LaguerreRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
