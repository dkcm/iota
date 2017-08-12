/**
 * SimpleLaguerreFilterTest.java  v0.2  13 May 2017 11:16:45 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SimpleLaguerreFilter</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class SimpleLaguerreFilterTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 4;

  public SimpleLaguerreFilterTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SimpleLaguerreFilterTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
