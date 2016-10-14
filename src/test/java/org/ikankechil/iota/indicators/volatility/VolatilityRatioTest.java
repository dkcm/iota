/**
 * VolatilityRatioTest.java  v0.1  3 October 2016 10:15:41 pm
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VolatilityRatio</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolatilityRatioTest extends AbstractIndicatorTest {

  public VolatilityRatioTest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VolatilityRatioTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
