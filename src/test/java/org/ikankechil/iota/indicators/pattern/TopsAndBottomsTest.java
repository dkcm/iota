/**
 * TopsAndBottomsTest.java  v0.3  31 December 2015 7:58:28 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TopsAndBottoms</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class TopsAndBottomsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_AWAY_POINTS = 5;

  public TopsAndBottomsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TopsAndBottomsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new TopsAndBottoms(DEFAULT_AWAY_POINTS, null, true);
  }

}
