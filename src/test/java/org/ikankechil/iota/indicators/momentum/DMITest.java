/**
 * DMITest.java v0.2 25 November 2015 3:17:50 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DMI</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DMITest extends AbstractIndicatorTest {

  public DMITest() {
    super(30);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DMITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new DMI(period, period);
  }

}
