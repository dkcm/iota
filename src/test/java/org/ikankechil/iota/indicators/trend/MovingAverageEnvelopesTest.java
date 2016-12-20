/**
 * MovingAverageEnvelopesTest.java  0.1  20 December 2016 4:34:33 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MovingAverageEnvelopes</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MovingAverageEnvelopesTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public MovingAverageEnvelopesTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MovingAverageEnvelopesTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
