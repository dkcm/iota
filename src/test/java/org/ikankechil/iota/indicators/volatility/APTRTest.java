/**
 * APTRTest.java  v0.1  24 April 2018 9:08:35 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>APTR</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class APTRTest extends AbstractIndicatorTest {

  public APTRTest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = APTRTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
