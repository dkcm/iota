/**
 * MoneyFlowTests.java  0.2  10 July 2017 10:22:23 PM
 *
 * Copyright © 2017-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volume.CMFTest;
import org.ikankechil.iota.indicators.volume.ForceIndexTest;
import org.ikankechil.iota.indicators.volume.MaddoxMomentumTest;
import org.ikankechil.iota.indicators.volume.MoneyFlowVolumeTest;
import org.ikankechil.iota.indicators.volume.TMFTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  // Money Flow
  CMFTest.class,
  MoneyFlowVolumeTest.class,
  TMFTest.class,

  // Force Index
  ForceIndexTest.class,
  MaddoxMomentumTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MoneyFlowTests {
  // holder for above annotations
}
