/**
 * HMATest.java  v0.2  10 October 2016 11:40:09 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>HMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class HMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 16;

  public HMATest() {
    super(DEFAULT_PERIOD - 1 + (int) Math.sqrt(DEFAULT_PERIOD) - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = HMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
