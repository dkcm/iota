/**
 * StochasticTest.java  v0.1 26 January 2016 8:19:47 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Stochastic</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class StochasticTest extends AbstractIndicatorTest {

  public StochasticTest() {
    super(17);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = StochasticTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
