/**
 * VolatilitySwitchIndicatorTest.java  v0.1  14 April 2018 12:15:09 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VolatilitySwitchIndicator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VolatilitySwitchIndicatorTest extends AbstractIndicatorTest {

  public VolatilitySwitchIndicatorTest() {
    super(41);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VolatilitySwitchIndicatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
