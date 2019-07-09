/**
 * MoneyFlowTests.java  0.5  10 July 2017 10:22:23 PM
 *
 * Copyright © 2017-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volume.CMFTest;
import org.ikankechil.iota.indicators.volume.ForceIndexTest;
import org.ikankechil.iota.indicators.volume.IntradayIntensityIndexTest;
import org.ikankechil.iota.indicators.volume.MaddoxMomentumTest;
import org.ikankechil.iota.indicators.volume.MoneyFlowVolumeTest;
import org.ikankechil.iota.indicators.volume.NVITest;
import org.ikankechil.iota.indicators.volume.PVITest;
import org.ikankechil.iota.indicators.volume.TMFTest;
import org.ikankechil.iota.indicators.volume.TVITest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  // Money Flow
  CMFTest.class,
  IntradayIntensityIndexTest.class,
  MoneyFlowVolumeTest.class,
  TMFTest.class,

  // Force Index
  ForceIndexTest.class,
  MaddoxMomentumTest.class,

  // Price Accumulation Volume Indicator
  NVITest.class,
  PVITest.class,

  // Trade Volume Index
  TVITest.class,
})

/**
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class MoneyFlowTests {
  // holder for above annotations
}
