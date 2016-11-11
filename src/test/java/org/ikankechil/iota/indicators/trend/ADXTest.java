/**
 * ADXTest.java  v0.1  7 November 2016 9:27:26 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ADX</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ADXTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public ADXTest() {
    super((2 * DEFAULT_PERIOD) - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ADXTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
