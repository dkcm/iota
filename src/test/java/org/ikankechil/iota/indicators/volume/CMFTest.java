/**
 * CMFTest.java  v0.2  10 January 2015 2:36:47 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CMF</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CMFTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 21;

  public CMFTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CMFTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
