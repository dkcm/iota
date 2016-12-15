/**
 * AVOTest.java  v0.1  15 December 2016 6:39:15 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>AVO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class AVOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_SLOW   = 26;
  private static final int DEFAULT_SIGNAL = 9;

  public AVOTest() {
    super((DEFAULT_SLOW - 1) + (DEFAULT_SIGNAL - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AVOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
