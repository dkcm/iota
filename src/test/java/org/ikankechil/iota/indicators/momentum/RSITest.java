/**
 * RSITest.java	v0.2	7 August 2015 5:42:00 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RSI</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public RSITest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = RSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
