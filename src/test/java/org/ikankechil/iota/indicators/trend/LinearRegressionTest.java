/**
 * LinearRegressionTest.java  v0.1  16 August 2017 11:23:54 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LinearRegression</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public LinearRegressionTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LinearRegressionTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
