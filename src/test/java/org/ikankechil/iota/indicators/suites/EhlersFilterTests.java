/**
 * EhlersFilterTests.java  v0.1  27 March 2019 12:03:13 am
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.momentum.CGOscillatorTest;
import org.ikankechil.iota.indicators.momentum.EhlersDistanceCoefficientFilterTest;
import org.ikankechil.iota.indicators.trend.ALMATest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ALMATest.class,
  CGOscillatorTest.class,
  EhlersDistanceCoefficientFilterTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class EhlersFilterTests {
  // holder for above annotations
}
