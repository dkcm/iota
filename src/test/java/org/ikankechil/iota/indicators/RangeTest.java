/**
 * RangeTest.java v0.2 25 November 2015 10:01:16 AM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>Range</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RangeTest extends AbstractIndicatorTest {

  public RangeTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = RangeTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override@Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
