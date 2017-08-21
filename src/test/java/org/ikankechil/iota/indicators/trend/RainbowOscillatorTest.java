/**
 * RainbowOscillatorTest.java  v0.1  22 August 2017 12:45:19 am
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RainbowOscillator</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RainbowOscillatorTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 2;

  public RainbowOscillatorTest() {
    super((DEFAULT_PERIOD - 1) * 10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RainbowOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
