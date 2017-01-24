/**
 * SMAROCTests.java  v0.1  17 January 2017 11:39:13 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.KSTTest;
import org.ikankechil.iota.indicators.momentum.KendallTest;
import org.ikankechil.iota.indicators.trend.SpecialKTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  KendallTest.class,
  KSTTest.class,
  SpecialKTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SMAROCTests {
  // holder for above annotations
}
