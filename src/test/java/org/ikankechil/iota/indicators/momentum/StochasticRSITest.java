/**
 * StochasticRSITest.java  v0.2 26 November 2015 2:26:11 PM
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
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class StochasticRSITest extends AbstractIndicatorTest {

  public StochasticRSITest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = StochasticRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new StochasticRSI(period, period, period);
  }

}
