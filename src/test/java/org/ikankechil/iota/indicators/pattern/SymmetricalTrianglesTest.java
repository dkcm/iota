/**
 * SymmetricalTrianglesTest.java  v0.1  8 June 2017 8:35:06 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>SymmetricalTriangles</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SymmetricalTrianglesTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 5;
  private static final int DEFAULT_THRESHOLD   = 3;
  private static final int DEFAULT_LOOKBACK    = 0;

  public SymmetricalTrianglesTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SymmetricalTrianglesTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new SymmetricalTriangles(DEFAULT_AWAY_POINTS, DEFAULT_THRESHOLD, Double.POSITIVE_INFINITY);
  }

}
