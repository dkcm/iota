/**
 * IndicatorsWithSignalLineTests.java  v0.1  15 December 2016 6:32:10 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.DCCITest;
import org.ikankechil.iota.indicators.trend.APOTest;
import org.ikankechil.iota.indicators.trend.KVOTest;
import org.ikankechil.iota.indicators.trend.PPOTest;
import org.ikankechil.iota.indicators.trend.TRIXTest;
import org.ikankechil.iota.indicators.volume.PVOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  APOTest.class,
  DCCITest.class,
  KVOTest.class,
//  NVITest.class,
  PPOTest.class,
  PVOTest.class,
  TRIXTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IndicatorsWithSignalLineTests {
  // holder for above annotations
}
