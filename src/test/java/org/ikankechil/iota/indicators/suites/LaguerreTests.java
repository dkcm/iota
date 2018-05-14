/**
 * LaguerreTests.java  v0.1  14 May 2018 9:27:33 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.LaguerreRSITest;
import org.ikankechil.iota.indicators.trend.SimpleLaguerreFilterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  SimpleLaguerreFilterTest.class,
  LaguerreRSITest.class,
})

/**
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LaguerreTests {
  // holder for above annotations
}
