/**
 * MinimumMaximumPriceIndexTest.java  v0.1  2 November 2016 7:06:12 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>MinimumMaximumPriceIndex</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinimumMaximumPriceIndexTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public MinimumMaximumPriceIndexTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MinimumMaximumPriceIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
