/**
 * TSITest.java  v0.2 26 November 2015 12:34:59 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TSITest extends AbstractIndicatorTest {

  public TSITest() {
    super(37);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new TSI(period, period);
  }

}
