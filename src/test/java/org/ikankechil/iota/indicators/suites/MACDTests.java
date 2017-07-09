/**
 * MACDTests.java  v0.3  6 December 2016 8:22:29 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.APOTest;
import org.ikankechil.iota.indicators.trend.DPOTest;
import org.ikankechil.iota.indicators.trend.MACDTest;
import org.ikankechil.iota.indicators.trend.PPOTest;
import org.ikankechil.iota.indicators.trend.TRIXTest;
import org.ikankechil.iota.indicators.trend.VWMACDTest;
import org.ikankechil.iota.indicators.trend.ZeroLagMACDTest;
import org.ikankechil.iota.indicators.trend.ZeroLagPPOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MACDTest.class,
  APOTest.class,
  PPOTest.class,
  DPOTest.class,
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
 * @version 0.3
 */
public class MACDTests {
  // holder class for above tests
}
