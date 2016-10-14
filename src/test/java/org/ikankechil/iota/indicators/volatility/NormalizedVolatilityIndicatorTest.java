/**
 * NormalizedVolatilityIndicatorTest.java  v0.1  4 October 2016 11:22:43 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>NormalizedVolatilityIndicator</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NormalizedVolatilityIndicatorTest extends AbstractIndicatorTest {

  public NormalizedVolatilityIndicatorTest() {
    super(64);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = NormalizedVolatilityIndicatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
