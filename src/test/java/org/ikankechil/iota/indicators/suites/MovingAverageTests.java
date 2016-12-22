/**
 * MovingAverageTests.java  v0.1  22 November 2016 12:25:27 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.suites;

import org.ikankechil.iota.indicators.trend.ALMATest;
import org.ikankechil.iota.indicators.trend.DEMATest;
import org.ikankechil.iota.indicators.trend.EMATest;
import org.ikankechil.iota.indicators.trend.GMMATest;
import org.ikankechil.iota.indicators.trend.GeneralisedDEMATest;
import org.ikankechil.iota.indicators.trend.HMATest;
import org.ikankechil.iota.indicators.trend.MOMATest;
import org.ikankechil.iota.indicators.trend.MovingAverageEnvelopesTest;
import org.ikankechil.iota.indicators.trend.SMATest;
import org.ikankechil.iota.indicators.trend.T3Test;
import org.ikankechil.iota.indicators.trend.TEMATest;
import org.ikankechil.iota.indicators.trend.TRIMATest;
import org.ikankechil.iota.indicators.trend.VOMATest;
import org.ikankechil.iota.indicators.trend.VOMOMATest;
import org.ikankechil.iota.indicators.trend.VWEMATest;
import org.ikankechil.iota.indicators.trend.WMATest;
import org.ikankechil.iota.indicators.trend.WSMATest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
  ALMATest.class,
  DEMATest.class,
  EMATest.class,
//  FRAMATest.class,
  GeneralisedDEMATest.class,
  GMMATest.class,
  HMATest.class,
//  KAMATest.class,
//  MAMATest.class,
  MOMATest.class,
  SMATest.class,
  T3Test.class,
  TEMATest.class,
  TRIMATest.class,
  VOMATest.class,
  VOMOMATest.class,
  VWEMATest.class,
//  WEVOMOTest.class,
  WMATest.class,
  WSMATest.class,
//  ZeroLagEMATest.class,
  MovingAverageEnvelopesTest.class,
})

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MovingAverageTests {
  // holder for above annotations
}
