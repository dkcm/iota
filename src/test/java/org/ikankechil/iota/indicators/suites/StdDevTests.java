/**
 * StdDevTests.java  v0.2  18 November 2016 9:44:08 pm
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volatility.StandardDeviationTest;
import org.ikankechil.iota.indicators.volatility.StandardErrorBandsTest;
import org.ikankechil.iota.indicators.volatility.StandardErrorTest;
import org.ikankechil.iota.indicators.volatility.VarianceTest;
import org.ikankechil.iota.indicators.volatility.ZScoreTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  StandardDeviationTest.class,
  VarianceTest.class,
  StandardErrorTest.class,
  StandardErrorBandsTest.class,
  ZScoreTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StdDevTests {
  // holder for above annotations
}
