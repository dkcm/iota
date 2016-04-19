/**
 * STARCBandsTest.java	v0.2	29 November 2015 12:13:01 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>STARCBands</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class STARCBandsTest extends AbstractIndicatorTest {

  public STARCBandsTest() {
    super(15);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = STARCBandsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new STARCBands(period, period);
  }

}
