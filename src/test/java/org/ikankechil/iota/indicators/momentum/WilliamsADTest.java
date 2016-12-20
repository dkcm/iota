/**
 * WilliamsADTest.java  0.1  20 December 2016 5:31:58 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>WilliamsAD</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WilliamsADTest extends AbstractIndicatorTest {

  public WilliamsADTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WilliamsADTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
