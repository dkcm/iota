/**
 * ZScoreTest.java  v0.1  16 April 2018 11:29:29 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.ikankechil.iota.indicators.Indicator;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ZScore</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZScoreTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public ZScoreTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZScoreTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Override
  public Indicator newInstance() throws ReflectiveOperationException {
    return new ZScore(DEFAULT_PERIOD);
  }

}
