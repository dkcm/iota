/**
 * MaximumPriceIndexTest.java  v0.1  2 November 2016 7:01:32 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>MaximumPriceIndex</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MaximumPriceIndexTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public MaximumPriceIndexTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MaximumPriceIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
