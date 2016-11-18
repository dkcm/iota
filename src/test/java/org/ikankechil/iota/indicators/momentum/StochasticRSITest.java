/**
 * StochasticRSITest.java  v0.3  26 November 2015 2:26:11 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>StochasticRSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class StochasticRSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;
  private static final int DEFAULT_K      = 5;
  private static final int DEFAULT_D      = 3;

  public StochasticRSITest() {
    super(DEFAULT_PERIOD + DEFAULT_K + DEFAULT_D - 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = StochasticRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new StochasticRSI(period, DEFAULT_K, DEFAULT_D);
  }

}
