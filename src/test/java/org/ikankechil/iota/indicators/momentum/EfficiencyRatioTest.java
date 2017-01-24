/**
 * EfficiencyRatioTest.java  v0.1  24 January 2017 12:22:08 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>EfficiencyRatio</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class EfficiencyRatioTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public EfficiencyRatioTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = EfficiencyRatioTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
