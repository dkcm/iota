/**
 * ChaikinVolatilityTest.java v0.1	10 September 2016 11:20:28 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ChaikinVolatility</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChaikinVolatilityTest extends AbstractIndicatorTest {

  public ChaikinVolatilityTest() {
    super(19);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ChaikinVolatilityTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
