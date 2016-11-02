/**
 * MinimumPriceTest.java  v0.1  2 November 2016 6:06:30 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>MinimumPrice</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumPriceTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public MinimumPriceTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MinimumPriceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
