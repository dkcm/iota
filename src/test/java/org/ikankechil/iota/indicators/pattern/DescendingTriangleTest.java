/**
 * DescendingTriangleTest.java  0.1  23 December 2016 10:05:34 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DescendingTriangle</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DescendingTriangleTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 5;
  private static final int DEFAULT_LOOKBACK    = 0;

  public DescendingTriangleTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DescendingTriangleTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new DescendingTriangle(new Trendlines(DEFAULT_AWAY_POINTS));
  }

}
