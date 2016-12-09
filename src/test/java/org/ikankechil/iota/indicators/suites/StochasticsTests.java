/**
 * StochasticsTests.java  v0.1  2 November 2016 10:30:33 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.FastStochasticTest;
import org.ikankechil.iota.indicators.momentum.KDJTest;
import org.ikankechil.iota.indicators.momentum.StochasticRSITest;
import org.ikankechil.iota.indicators.momentum.StochasticTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  StochasticTest.class,
  FastStochasticTest.class,
  KDJTest.class,
  StochasticRSITest.class
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StochasticsTests {
  // holder for above annotations
}
