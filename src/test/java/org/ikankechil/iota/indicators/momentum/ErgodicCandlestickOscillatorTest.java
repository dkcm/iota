/**
 * ErgodicCandlestickOscillatorTest.java  v0.1  15 December 2016 12:02:17 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ErgodicCandlestickOscillator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ErgodicCandlestickOscillatorTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SHORT = 12;
  private static final int DEFAULT_LONG  = 32;

  public ErgodicCandlestickOscillatorTest() {
    super((DEFAULT_SHORT - 1) + (DEFAULT_LONG - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ErgodicCandlestickOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
