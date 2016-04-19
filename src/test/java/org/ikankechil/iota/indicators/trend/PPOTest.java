/**
 * PPOTest.java	v0.2	11 December 2015 3:53:47 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>PPO</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PPOTest extends AbstractIndicatorTest {

  public PPOTest() {
    super(33);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = PPOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new PPO(period, 26, 9);
  }

}
