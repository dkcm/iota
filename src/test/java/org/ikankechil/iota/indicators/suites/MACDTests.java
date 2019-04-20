/**
 * MACDTests.java  v0.4  6 December 2016 8:22:29 pm
 *
 * Copyright © 2016-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.APOTest;
import org.ikankechil.iota.indicators.trend.DPOTest;
import org.ikankechil.iota.indicators.trend.MACDTest;
import org.ikankechil.iota.indicators.trend.PPOTest;
import org.ikankechil.iota.indicators.trend.SchaffTrendCycleTest;
import org.ikankechil.iota.indicators.trend.TRIXTest;
import org.ikankechil.iota.indicators.trend.VWMACDTest;
import org.ikankechil.iota.indicators.trend.ZeroLagMACDTest;
import org.ikankechil.iota.indicators.trend.ZeroLagPPOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  APOTest.class,
  DPOTest.class,
  MACDTest.class,
  PPOTest.class,
  SchaffTrendCycleTest.class,
  TRIXTest.class,
  VWMACDTest.class,
  ZeroLagMACDTest.class,
  ZeroLagPPOTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.4
 */
public class MACDTests {
  // holder for above annotations
}
