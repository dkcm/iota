/**
 * SchaffTrendCycleTest.java  v0.1  14 April 2019 3:59:12 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code SchaffTrendCycle}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SchaffTrendCycleTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 85;

  public SchaffTrendCycleTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SchaffTrendCycleTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
