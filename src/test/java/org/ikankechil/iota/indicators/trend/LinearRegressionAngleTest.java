/**
 * LinearRegressionAngleTest.java  v0.1  20 August 2017 11:54:32 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LinearRegressionAngle</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionAngleTest extends LinearRegressionTest {

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LinearRegressionAngleTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
