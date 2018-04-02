/**
 * BollingerBandsTests.java  v0.3  17 November 2016 12:32:22 am
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.BollingerBTest;
import org.ikankechil.iota.indicators.momentum.SVEBollingerBTest;
import org.ikankechil.iota.indicators.volatility.BetterBollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.BollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.BollingerBandwidthTest;
import org.ikankechil.iota.indicators.volatility.VolatilityBandsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  BollingerBandsTest.class,
  BetterBollingerBandsTest.class,
  BollingerBandwidthTest.class,
  BollingerBTest.class,
  SVEBollingerBTest.class,
  VolatilityBandsTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class BollingerBandsTests {
  // holder for above annotations
}
