/**
 * TimeSeriesForecastTest.java  v0.1  14 April 2019 11:54:07 PM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code TimeSeriesForecast}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TimeSeriesForecastTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 13;

  public TimeSeriesForecastTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TimeSeriesForecastTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
