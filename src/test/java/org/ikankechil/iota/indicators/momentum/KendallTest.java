/**
 * KendallTest.java v0.2 25 November 2015 3:04:23 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Kendall</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class KendallTest extends AbstractIndicatorTest {

  public KendallTest() {
    super(16);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = KendallTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new Kendall(period, period);
  }

}
