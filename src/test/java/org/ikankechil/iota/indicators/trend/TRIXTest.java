/**
 * TRIXTest.java  v0.1  15 December 2016 1:28:21 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TRIX</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TRIXTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 5;
  private static final int DEFAULT_SIGNAL = 9;

  public TRIXTest() {
    super((DEFAULT_PERIOD - 1) * 3 + (DEFAULT_SIGNAL - 1) + 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TRIXTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
