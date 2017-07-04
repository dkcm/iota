/**
 * DerivativeOscillatorTest.java  v0.1  4 July 2017 10:25:09 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DerivativeOscillator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DerivativeOscillatorTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = (14 + 5 + 3 + 9) - 3;

  public DerivativeOscillatorTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DerivativeOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
