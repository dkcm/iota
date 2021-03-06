/**
 * BollingerBandsTest.java  v0.2  28 November 2015 9:33:18 pm
 *
 * Copyright � 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>BollingerBands</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class BollingerBandsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public BollingerBandsTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = BollingerBandsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new BollingerBands(period, period);
  }

}
