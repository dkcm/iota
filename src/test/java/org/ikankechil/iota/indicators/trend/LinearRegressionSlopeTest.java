/**
 * LinearRegressionSlopeTest.java  v0.1  18 August 2017 5:50:55 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LinearRegressionSlope</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionSlopeTest extends LinearRegressionTest {

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LinearRegressionSlopeTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
