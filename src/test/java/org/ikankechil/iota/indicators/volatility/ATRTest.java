/**
 * ATRTest.java  v0.2 10 January 2015 2:41:41 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ATR</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ATRTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public ATRTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ATRTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
