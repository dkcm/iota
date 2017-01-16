/**
 * OscillatorTests.java  v0.1  10 January 2017 1:23:40 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.AcceleratorOscillatorTest;
import org.ikankechil.iota.indicators.momentum.AwesomeOscillatorTest;
import org.ikankechil.iota.indicators.momentum.CMOTest;
import org.ikankechil.iota.indicators.momentum.ErgodicCandlestickOscillatorTest;
import org.ikankechil.iota.indicators.momentum.PGOTest;
import org.ikankechil.iota.indicators.momentum.PZOTest;
import org.ikankechil.iota.indicators.momentum.TLMOTest;
import org.ikankechil.iota.indicators.momentum.UltimateOscillatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  AcceleratorOscillatorTest.class,
  AwesomeOscillatorTest.class,
//  CGOscillatorTest.class,
  CMOTest.class,
  ErgodicCandlestickOscillatorTest.class,
//  KasePeakOscillatorTest.class,
  PGOTest.class,
  PZOTest.class,
  TLMOTest.class,
  UltimateOscillatorTest.class,
})

/**
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OscillatorTests {
  // holder for above annotations
}
