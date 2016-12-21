/**
 * TopsTest.java  v0.1  8 January 2016 12:09:20 am
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.pattern;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Tops</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TopsTest extends AbstractIndicatorTest {

  private static final int DEFAULT_LOOKBACK = 0;

  public TopsTest() {
    super(DEFAULT_LOOKBACK);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TopsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
