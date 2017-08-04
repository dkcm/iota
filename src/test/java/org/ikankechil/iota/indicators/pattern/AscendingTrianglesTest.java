/**
 * AscendingTrianglesTest.java  0.2  23 December 2016 10:05:22 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AscendingTriangles</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AscendingTrianglesTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 5;
  private static final int DEFAULT_THRESHOLD   = 3;
  private static final int DEFAULT_LOOKBACK    = 0;

  public AscendingTrianglesTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AscendingTrianglesTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new AscendingTriangles(DEFAULT_AWAY_POINTS, DEFAULT_THRESHOLD, Double.POSITIVE_INFINITY);
  }

}
