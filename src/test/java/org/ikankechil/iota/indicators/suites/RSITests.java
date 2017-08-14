/**
 * RSITests.java  v0.2  16 November 2016 10:02:34 pm
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.AsymmetricalRSITest;
import org.ikankechil.iota.indicators.momentum.DMITest;
import org.ikankechil.iota.indicators.momentum.DerivativeOscillatorTest;
import org.ikankechil.iota.indicators.momentum.KRITest;
import org.ikankechil.iota.indicators.momentum.LaguerreRSITest;
import org.ikankechil.iota.indicators.momentum.MFITest;
import org.ikankechil.iota.indicators.momentum.NMOTest;
import org.ikankechil.iota.indicators.momentum.RMITest;
import org.ikankechil.iota.indicators.momentum.RSITest;
import org.ikankechil.iota.indicators.momentum.SVEInverseFisherRSITest;
import org.ikankechil.iota.indicators.momentum.SelfAdjustingRSITest;
import org.ikankechil.iota.indicators.momentum.SlowRSITest;
import org.ikankechil.iota.indicators.momentum.StochasticRSITest;
import org.ikankechil.iota.indicators.momentum.TSITest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  RSITest.class,
  AsymmetricalRSITest.class,
  LaguerreRSITest.class,
  SelfAdjustingRSITest.class,
  SlowRSITest.class,
  StochasticRSITest.class,
  SVEInverseFisherRSITest.class,
  DMITest.class,
  KRITest.class,
  MFITest.class,
  NMOTest.class,
  RMITest.class,
  TSITest.class,
  DerivativeOscillatorTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RSITests {
  // holder for above annotations
}
