/**
 * PriceTests.java  v0.2  25 February 2018 10:05:06 AM
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
import org.ikankechil.iota.indicators.volume.MVWAPTest;
import org.ikankechil.iota.indicators.volume.VWAPTest;
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
  MVWAPTest.class,
  OpenPriceTest.class,
  TypicalPriceTest.class,
  VWAPTest.class,
  WeightedClosePriceTest.class,
})

/**
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PriceTests {
  // holder for above annotations
}
