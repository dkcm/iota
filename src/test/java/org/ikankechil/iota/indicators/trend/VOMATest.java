/**
 * VOMATest.java  v0.2  9 October 2016 2:28:51 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VOMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VOMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 4;

  public VOMATest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VOMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
