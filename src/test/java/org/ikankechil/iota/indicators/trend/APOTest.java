/**
 * APOTest.java  v0.1  6 December 2016 8:24:17 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>APO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class APOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SLOW   = 26;
  private static final int DEFAULT_SIGNAL = 9;

  public APOTest() {
    super((DEFAULT_SLOW - 1) + (DEFAULT_SIGNAL - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = APOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
