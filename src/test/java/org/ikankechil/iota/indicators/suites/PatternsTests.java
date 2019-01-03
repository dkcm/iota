/**
 * PatternsTests.java  v0.5  22 November 2016 9:49:14 pm
 *
 * Copyright © 2016-2019 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.pattern.AscendingTrianglesTest;
import org.ikankechil.iota.indicators.pattern.BottomsTest;
import org.ikankechil.iota.indicators.pattern.DescendingTrianglesTest;
import org.ikankechil.iota.indicators.pattern.DivergenceIndexTest;
import org.ikankechil.iota.indicators.pattern.DivergenceTest;
import org.ikankechil.iota.indicators.pattern.DowntrendChannelsTest;
import org.ikankechil.iota.indicators.pattern.FallingWedgesTest;
import org.ikankechil.iota.indicators.pattern.GapsTest;
import org.ikankechil.iota.indicators.pattern.HeadAndShouldersTest;
import org.ikankechil.iota.indicators.pattern.RectanglesTest;
import org.ikankechil.iota.indicators.pattern.RisingWedgesTest;
import org.ikankechil.iota.indicators.pattern.SymmetricalTrianglesTest;
import org.ikankechil.iota.indicators.pattern.TopsAndBottomsTest;
import org.ikankechil.iota.indicators.pattern.TopsTest;
import org.ikankechil.iota.indicators.pattern.TrendlineTest;
import org.ikankechil.iota.indicators.pattern.TrendlinesTest;
import org.ikankechil.iota.indicators.pattern.UptrendChannelsTest;
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
  GapsTest.class,
  TrendlineTest.class,
  TrendlinesTest.class,
  HeadAndShouldersTest.class,
  AscendingTrianglesTest.class,
  DescendingTrianglesTest.class,
  SymmetricalTrianglesTest.class,
  FallingWedgesTest.class,
  RisingWedgesTest.class,
  DowntrendChannelsTest.class,
  UptrendChannelsTest.class,
  RectanglesTest.class
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.5
 */
public class PatternsTests {
  // holder for above annotations
}
