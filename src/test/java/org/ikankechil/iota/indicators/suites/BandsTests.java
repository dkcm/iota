/**
 * BandsTests.java  v0.1  24 September 2017 11:51:36 AM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volatility.AccelerationBandsTest;
import org.ikankechil.iota.indicators.volatility.BetterBollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.BollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.DonchianChannelsTest;
import org.ikankechil.iota.indicators.volatility.STARCBandsTest;
import org.ikankechil.iota.indicators.volatility.StandardErrorBandsTest;
import org.ikankechil.iota.indicators.volatility.VolatilityBandsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  AccelerationBandsTest.class,
  BetterBollingerBandsTest.class,
  BollingerBandsTest.class,
  DonchianChannelsTest.class,
//  KirshenbaumBandsTest.class,
  StandardErrorBandsTest.class,
  STARCBandsTest.class,
  VolatilityBandsTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BandsTests {
  // holder for above annotations
}
