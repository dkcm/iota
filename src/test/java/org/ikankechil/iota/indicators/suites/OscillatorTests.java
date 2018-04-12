/**
 * OscillatorTests.java  v0.3  10 January 2017 1:23:40 am
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.AcceleratorOscillatorTest;
import org.ikankechil.iota.indicators.momentum.AwesomeOscillatorTest;
import org.ikankechil.iota.indicators.momentum.CGOscillatorTest;
import org.ikankechil.iota.indicators.momentum.CMOTest;
import org.ikankechil.iota.indicators.momentum.DerivativeOscillatorTest;
import org.ikankechil.iota.indicators.momentum.ErgodicCandlestickOscillatorTest;
import org.ikankechil.iota.indicators.momentum.PGOTest;
import org.ikankechil.iota.indicators.momentum.PZOTest;
import org.ikankechil.iota.indicators.momentum.TLMOTest;
import org.ikankechil.iota.indicators.momentum.UltimateOscillatorTest;
import org.ikankechil.iota.indicators.trend.AroonOscillatorTest;
import org.ikankechil.iota.indicators.trend.RainbowOscillatorTest;
import org.ikankechil.iota.indicators.volume.AVOTest;
import org.ikankechil.iota.indicators.volume.ChaikinOscillatorTest;
import org.ikankechil.iota.indicators.volume.PVOTest;
import org.ikankechil.iota.indicators.volume.VZOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  // Momentum indicators
  AcceleratorOscillatorTest.class,
  AwesomeOscillatorTest.class,
  CGOscillatorTest.class,
  CMOTest.class,
  DerivativeOscillatorTest.class,
  ErgodicCandlestickOscillatorTest.class,
//  KasePeakOscillatorTest.class,
  PGOTest.class,
  PZOTest.class,
  TLMOTest.class,
  UltimateOscillatorTest.class,

  // Trend indicators
  AroonOscillatorTest.class,
  RainbowOscillatorTest.class,

  // Volume indicators
  AVOTest.class,
  ChaikinOscillatorTest.class,
  PVOTest.class,
  VZOTest.class,
})

/**
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class OscillatorTests {
  // holder for above annotations
}
