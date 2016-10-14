/**
 * VIDYATest.java  v0.2  11 July 2015 10:47:37 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VIDYA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class VIDYATest extends AbstractIndicatorTest {

  public VIDYATest() {
    super(7);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VIDYATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new VIDYA(period, 9);
  }

}
