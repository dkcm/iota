/**
 * MOMATest.java  v0.2  9 October 2016 12:06:23 am
 *
 * Copyright � 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MOMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MOMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 4;

  public MOMATest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MOMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
