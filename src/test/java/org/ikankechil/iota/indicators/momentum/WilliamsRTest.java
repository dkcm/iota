/**
 * WilliamsRTest.java  v0.1 26 January 2016 8:16:35 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>WilliamsR</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WilliamsRTest extends AbstractIndicatorTest {

  public WilliamsRTest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WilliamsRTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
