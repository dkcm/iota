/**
 * TLMOTest.java  v0.1  4 January 2017 3:03:04 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TLMO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TLMOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD    = 10;
  private static final int DEFAULT_MOMENTUM  = 5;
  private static final int DEFAULT_SMOOTHING = 5;

  public TLMOTest() {
    super((DEFAULT_PERIOD - 1) + DEFAULT_MOMENTUM + (DEFAULT_SMOOTHING - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TLMOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
