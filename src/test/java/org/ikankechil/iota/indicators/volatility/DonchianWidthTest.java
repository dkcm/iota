/**
 * DonchianWidthTest.java  v0.1  10 May 2019 9:05:57 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code DonchianWidth}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DonchianWidthTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public DonchianWidthTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DonchianWidthTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
