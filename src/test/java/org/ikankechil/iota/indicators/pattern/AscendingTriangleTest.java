/**
 * AscendingTriangleTest.java  0.1  23 December 2016 10:05:22 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AscendingTriangle</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AscendingTriangleTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 15;
  private static final int DEFAULT_LOOKBACK    = 0;

  public AscendingTriangleTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AscendingTriangleTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new AscendingTriangle(new Trendlines(DEFAULT_AWAY_POINTS));
  }

}
