/**
 * StopsTest.java  v0.2  31 March 2018 2:20:17 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.stops.CamarillaPivotPointsTest;
import org.ikankechil.iota.indicators.stops.FibonacciRetracementsTest;
import org.ikankechil.iota.indicators.stops.KaufmanVolatilityStopsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  CamarillaPivotPointsTest.class,
  FibonacciRetracementsTest.class,
  KaufmanVolatilityStopsTest.class
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StopsTest {
  // holder for above annotations
}
