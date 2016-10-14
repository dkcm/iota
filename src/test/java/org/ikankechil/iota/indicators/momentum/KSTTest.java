/**
 * KSTTest.java  v0.2  11 July 2015 4:25:23 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KST</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KSTTest extends AbstractIndicatorTest {

  public KSTTest() {
    super(52);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KSTTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new KST(period, period, period, period, period, period, period, period, period);
  }

}
