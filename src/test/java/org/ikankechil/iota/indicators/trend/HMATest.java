/**
 * HMATest.java  v0.1  10 October 2016 11:40:09 pm
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>HMA</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HMATest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 16;

  public HMATest() {
    super(DEFAULT_PERIOD - 1 + (int) Math.sqrt(DEFAULT_PERIOD) - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = HMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
