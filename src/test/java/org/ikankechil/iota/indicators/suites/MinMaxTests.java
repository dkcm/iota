/**
 * MinMaxTests.java  v0.1  15 November 2016 11:42:33 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.MaximumPriceIndexTest;
import org.ikankechil.iota.indicators.MaximumPriceTest;
import org.ikankechil.iota.indicators.MinimumMaximumPriceIndexTest;
import org.ikankechil.iota.indicators.MinimumMaximumPriceTest;
import org.ikankechil.iota.indicators.MinimumPriceIndexTest;
import org.ikankechil.iota.indicators.MinimumPriceTest;
import org.ikankechil.iota.indicators.trend.VHFTest;
import org.ikankechil.iota.indicators.volatility.DonchianChannelsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  MinimumPriceTest.class,
  MinimumPriceIndexTest.class,
  MaximumPriceTest.class,
  MaximumPriceIndexTest.class,
  MinimumMaximumPriceTest.class,
  MinimumMaximumPriceIndexTest.class,
  DonchianChannelsTest.class,
  VHFTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinMaxTests {
  // holder for above annotations
}
