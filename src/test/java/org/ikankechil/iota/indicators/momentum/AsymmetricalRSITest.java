/**
 * AsymmetricalRSITest.java	v0.2	23 November 2015 3:27:37 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AsymmetricalRSI</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AsymmetricalRSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public AsymmetricalRSITest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = AsymmetricalRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
