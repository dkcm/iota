/**
 * RSITests.java  v0.1  16 November 2016 10:02:34 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.DMITest;
import org.ikankechil.iota.indicators.momentum.LaguerreRSITest;
import org.ikankechil.iota.indicators.momentum.MFITest;
import org.ikankechil.iota.indicators.momentum.RSITest;
import org.ikankechil.iota.indicators.momentum.SelfAdjustingRSITest;
import org.ikankechil.iota.indicators.momentum.StochasticRSITest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  RSITest.class,
//  AsymmetricalRSITest.class,
  LaguerreRSITest.class,
  SelfAdjustingRSITest.class,
//  SlowRSITest.class,
  StochasticRSITest.class,
  DMITest.class,
  MFITest.class
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RSITests {
  // holder for above annotations
}
