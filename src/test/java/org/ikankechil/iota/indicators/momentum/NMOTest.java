/**
 * NMOTest.java  v0.1  30 June 2017 6:49:08 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>NMO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NMOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public NMOTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = NMOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
