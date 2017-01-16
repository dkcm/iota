/**
 * KAMATest.java  v0.1  10 January 2017 9:45:01 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KAMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KAMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 30;

  public KAMATest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KAMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
