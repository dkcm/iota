/**
 * DivergenceIndexTest.java v0.1 21 April 2016 10:37:36 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.momentum.RSI;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DivergenceIndex</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DivergenceIndexTest extends AbstractIndicatorTest {

  private static final Indicator INDICATOR = new RSI();

  public DivergenceIndexTest() {
    super(INDICATOR.lookback());
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = DivergenceIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new DivergenceIndex(INDICATOR);
  }

}
