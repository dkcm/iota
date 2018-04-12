/**
 * IndicatorsWithSignalLineTests.java  v0.3  15 December 2016 6:32:10 pm
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.DCCITest;
import org.ikankechil.iota.indicators.momentum.QStickTest;
import org.ikankechil.iota.indicators.trend.APOTest;
import org.ikankechil.iota.indicators.trend.KVOTest;
import org.ikankechil.iota.indicators.trend.MACDTest;
import org.ikankechil.iota.indicators.trend.PPOTest;
import org.ikankechil.iota.indicators.trend.TRIXTest;
import org.ikankechil.iota.indicators.trend.VWMACDTest;
import org.ikankechil.iota.indicators.volume.AVOTest;
import org.ikankechil.iota.indicators.volume.NVITest;
import org.ikankechil.iota.indicators.volume.PVITest;
import org.ikankechil.iota.indicators.volume.PVOTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  APOTest.class,
  AVOTest.class,
  DCCITest.class,
  KVOTest.class,
  MACDTest.class,
  NVITest.class,
  PPOTest.class,
  PVITest.class,
  PVOTest.class,
  QStickTest.class,
  TRIXTest.class,
  VWMACDTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class IndicatorsWithSignalLineTests {
  // holder for above annotations
}
