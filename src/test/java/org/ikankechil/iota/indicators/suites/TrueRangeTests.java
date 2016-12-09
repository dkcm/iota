/**
 * TrueRangeTests.java  v0.1  26 October 2016 10:54:23 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.VortexTest;
import org.ikankechil.iota.indicators.volatility.ATRTest;
import org.ikankechil.iota.indicators.volatility.NATRTest;
import org.ikankechil.iota.indicators.volatility.NormalizedVolatilityIndicatorTest;
import org.ikankechil.iota.indicators.volatility.TRTest;
import org.ikankechil.iota.indicators.volatility.VolatilityRatioTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  TRTest.class,
  ATRTest.class,
  NATRTest.class,
  NormalizedVolatilityIndicatorTest.class,
//  RWITest.class,
  VortexTest.class,
  VolatilityRatioTest.class
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrueRangeTests {
  // holder for above annotations
}
