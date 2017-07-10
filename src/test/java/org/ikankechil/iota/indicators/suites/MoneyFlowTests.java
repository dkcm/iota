/**
 * MoneyFlowTests.java  0.1  Jul 10, 2017 10:22:23 PM
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.volume.CMFTest;
import org.ikankechil.iota.indicators.volume.MoneyFlowVolumeTest;
import org.ikankechil.iota.indicators.volume.TMFTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  CMFTest.class,
  MoneyFlowVolumeTest.class,
  TMFTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MoneyFlowTests {
  // holder for above annotations
}
