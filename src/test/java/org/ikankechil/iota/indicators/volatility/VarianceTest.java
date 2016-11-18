/**
 * VarianceTest.java  v0.1  18 November 2016 12:53:35 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Variance</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VarianceTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 5;

  public VarianceTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VarianceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return newInstance(DEFAULT_PERIOD);
  }

}
