/**
 * TrendlinesTest.java v0.1 Jan 27, 2016 12:10:35 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.pattern.Trendlines;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Trendlines</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TrendlinesTest extends AbstractIndicatorTest {

  public TrendlinesTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = TrendlinesTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new Trendlines(5);
  }

}
