/**
 * SelfAdjustingRSITest.java	v0.2	13 December 2015 10:49:54 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SelfAdjustingRSI</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class SelfAdjustingRSITest extends AbstractIndicatorTest {

  public SelfAdjustingRSITest() {
    super(27);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = SelfAdjustingRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
