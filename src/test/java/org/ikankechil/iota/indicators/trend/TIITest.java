/**
 * TIITest.java  v0.2  9 December 2015 12:43:27 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TII</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TIITest extends AbstractIndicatorTest {

  public TIITest() {
    super(88);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TIITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new TII(period, period);
  }

}
