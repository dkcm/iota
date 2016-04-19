/**
 * MultiVoteOBVTest.java v0.2 24 November 2015 4:46:06 PM
 *
 * Copyright � 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test for <code>MultiVoteOBV</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MultiVoteOBVTest extends AbstractIndicatorTest {

  public MultiVoteOBVTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = MultiVoteOBVTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override@Test
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
