/**
 * DirectionalMovementIndexTests.java  v0.2  9 November 2016 12:06:46 am
 *
 * Copyright © 2016-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.ADXRTest;
import org.ikankechil.iota.indicators.trend.ADXTest;
import org.ikankechil.iota.indicators.trend.DXTest;
import org.ikankechil.iota.indicators.trend.MinusDITest;
import org.ikankechil.iota.indicators.trend.MinusDMTest;
import org.ikankechil.iota.indicators.trend.PlusDITest;
import org.ikankechil.iota.indicators.trend.PlusDMTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  PlusDMTest.class,
  PlusDITest.class,
  MinusDMTest.class,
  MinusDITest.class,
  DXTest.class,
  ADXTest.class,
  ADXRTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DirectionalMovementIndexTests {
  // holder for above annotations
}
