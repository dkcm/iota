/**
 * LinearRegressionInterceptTest.java  v0.1  20 August 2017 11:58:10 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LinearRegressionIntercept</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionInterceptTest extends LinearRegressionTest {

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LinearRegressionInterceptTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
