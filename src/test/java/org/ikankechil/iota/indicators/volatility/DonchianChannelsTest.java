/**
 * DonchianChannelsTest.java  0.1  20 December 2016 3:31:01 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DonchianChannels</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DonchianChannelsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public DonchianChannelsTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DonchianChannelsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
