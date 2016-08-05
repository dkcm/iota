/**
 * DivergenceTest.java  v0.1  21 April 2016 10:47:38 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.ikankechil.iota.indicators.momentum.RSI;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Divergence</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DivergenceTest extends AbstractIndicatorTest {

  private static final Indicator INDICATOR = new RSI();

  public DivergenceTest() {
    super(INDICATOR.lookback());
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DivergenceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() {
    return new Divergence(INDICATOR);
  }

}
