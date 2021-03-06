/**
 * HeadAndShouldersTest.java  v0.1  5 June 2017 9:48:52 pm
 *
 * Copyright � 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>HeadAndShoulders</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HeadAndShouldersTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 5;
  private static final int DEFAULT_LOOKBACK    = 0;

  public HeadAndShouldersTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = HeadAndShouldersTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new HeadAndShoulders(DEFAULT_AWAY_POINTS);
  }

}
