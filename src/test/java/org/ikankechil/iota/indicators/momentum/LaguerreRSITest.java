/**
 * LaguerreRSITest.java  v0.1  18 September 2016 11:49:06 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>LaguerreRSI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class LaguerreRSITest extends AbstractIndicatorTest {

  public LaguerreRSITest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = LaguerreRSITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
