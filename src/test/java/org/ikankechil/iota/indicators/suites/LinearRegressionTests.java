/**
 * LinearRegressionTests.java  v0.1  20 August 2017 11:52:42 pm
 *
 * Copyright � 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.LinearRegressionAngleTest;
import org.ikankechil.iota.indicators.trend.LinearRegressionInterceptTest;
import org.ikankechil.iota.indicators.trend.LinearRegressionSlopeTest;
import org.ikankechil.iota.indicators.trend.LinearRegressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  LinearRegressionTest.class,
  LinearRegressionAngleTest.class,
  LinearRegressionInterceptTest.class,
  LinearRegressionSlopeTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LinearRegressionTests {
  // holder for above annotations
}