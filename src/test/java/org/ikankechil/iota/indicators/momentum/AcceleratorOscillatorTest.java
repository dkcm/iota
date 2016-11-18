/**
 * AcceleratorOscillatorTest.java  v0.1  16 October 2016 11:41:40 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AcceleratorOscillator</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AcceleratorOscillatorTest extends AbstractIndicatorTest {

  private static final int DEFAULT_FAST = 5;
  private static final int DEFAULT_SLOW = 34;

  public AcceleratorOscillatorTest() {
    super(DEFAULT_FAST + DEFAULT_SLOW - 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AcceleratorOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
