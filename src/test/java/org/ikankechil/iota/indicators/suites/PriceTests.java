/**
 * PriceTests.java  v0.1  25 February 2018 10:05:06 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.AveragePriceTest;
import org.ikankechil.iota.indicators.ClosePriceTest;
import org.ikankechil.iota.indicators.HighPriceTest;
import org.ikankechil.iota.indicators.LowPriceTest;
import org.ikankechil.iota.indicators.MedianPriceTest;
import org.ikankechil.iota.indicators.OpenPriceTest;
import org.ikankechil.iota.indicators.TypicalPriceTest;
import org.ikankechil.iota.indicators.WeightedClosePriceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  AveragePriceTest.class,
  ClosePriceTest.class,
  HighPriceTest.class,
  LowPriceTest.class,
  MedianPriceTest.class,
  OpenPriceTest.class,
  TypicalPriceTest.class,
  WeightedClosePriceTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PriceTests {
  // holder for above annotations
}
