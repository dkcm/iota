/**
 * ForecastOscillatorTest.java  v0.1  14 April 2019 9:16:16 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code ForecastOscillator}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ForecastOscillatorTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 13;

  public ForecastOscillatorTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ForecastOscillatorTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
