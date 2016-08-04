/**
 * BollingerBTest.java	v0.2	28 November 2015 9:36:25 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>BollingerB</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class BollingerBTest extends AbstractIndicatorTest {

  public BollingerBTest() {
    super(19);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = BollingerBTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance(final int period) {
    return new BollingerB(period, 2);
  }

}
