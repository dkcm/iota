/**
 * GapsTest.java  v0.1  3 January 2019 9:11:24 PM
 *
 * Copyright © 2019 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code Gaps}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GapsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 1;

  public GapsTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GapsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
