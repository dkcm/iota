/**
 * TimeSeriesTests.java  v0.1  13 January 2017 11:06:19 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.HeikinAshiTimeSeriesTest;
import org.ikankechil.iota.OHLCVTimeSeriesTest;
import org.ikankechil.iota.SignalTimeSeriesTest;
import org.ikankechil.iota.TimeSeriesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  TimeSeriesTest.class,
  OHLCVTimeSeriesTest.class,
  HeikinAshiTimeSeriesTest.class,
  SignalTimeSeriesTest.class,
//  TrendlineTimeSeriesTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TimeSeriesTests {
  // holder for above annotations
}
