/**
 * FastStochasticTest.java  v0.1 26 January 2016 8:22:45 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>FastStochastic</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class FastStochasticTest extends AbstractIndicatorTest {

  public FastStochasticTest() {
    super(15);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = FastStochasticTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
