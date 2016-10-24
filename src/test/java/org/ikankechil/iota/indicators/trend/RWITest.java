/**
 * RWITest.java  v0.1  24 October 2016 10:32:01 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RWI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RWITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 64;

  public RWITest() {
    super(DEFAULT_PERIOD + 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RWITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
