/**
 * PatternsTests.java  v0.1  22 November 2016 9:49:14 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.pattern.BottomsTest;
import org.ikankechil.iota.indicators.pattern.DivergenceIndexTest;
import org.ikankechil.iota.indicators.pattern.DivergenceTest;
import org.ikankechil.iota.indicators.pattern.TopsAndBottomsTest;
import org.ikankechil.iota.indicators.pattern.TopsTest;
import org.ikankechil.iota.indicators.pattern.TrendlineTest;
import org.ikankechil.iota.indicators.pattern.TrendlinesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  TopsTest.class,
  BottomsTest.class,
  TopsAndBottomsTest.class,
  DivergenceTest.class,
  DivergenceIndexTest.class,
  TrendlineTest.class,
  TrendlinesTest.class
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PatternsTests {
  // holder for above annotations
}
