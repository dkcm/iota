/**
 * LaguerreRSITest.java  v0.3  18 September 2016 11:49:06 pm
 *
 * Copyright © 2016-2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import static org.junit.Assert.*;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test for <code>LaguerreRSI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class LaguerreRSITest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 4;

  public LaguerreRSITest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LaguerreRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Test
  public void zeroCuPlusCd() {
    assertEquals(0, new LaguerreRSI().filter(0, 0, 0, 0), 0);
  }

}
