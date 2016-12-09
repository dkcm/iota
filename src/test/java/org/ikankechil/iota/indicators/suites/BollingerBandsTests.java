/**
 * BollingerBandsTests.java  v0.1  17 November 2016 12:32:22 am
 *
 * Copyright � 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.BollingerBTest;
import org.ikankechil.iota.indicators.volatility.BetterBollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.BollingerBandsTest;
import org.ikankechil.iota.indicators.volatility.BollingerBandwidthTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  BollingerBandsTest.class,
  BetterBollingerBandsTest.class,
  BollingerBandwidthTest.class,
  BollingerBTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class BollingerBandsTests {
  // holder for above annotations
}
