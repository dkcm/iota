/**
 * SpecialKTest.java  v0.1  17 January 2017 11:41:15 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SpecialK</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SpecialKTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 725;

  public SpecialKTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SpecialKTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
