/**
 * RVITest.java  v0.2 1 December 2015 7:34:58 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RVI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RVITest extends AbstractIndicatorTest {

  public RVITest() {
    super(23);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RVITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new RVI(period, period);
  }

}
