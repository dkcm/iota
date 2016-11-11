/**
 * ADXRTest.java  v0.1  7 November 2016 9:27:03 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ADXR</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ADXRTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public ADXRTest() {
    super((3 * DEFAULT_PERIOD) - 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ADXRTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
