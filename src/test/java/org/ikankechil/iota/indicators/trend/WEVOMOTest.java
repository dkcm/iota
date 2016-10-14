/**
 * WEVOMOTest.java  v0.1  9 October 2016 11:17:20 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>WEVOMO</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class WEVOMOTest extends AbstractIndicatorTest {

  public WEVOMOTest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = WEVOMOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
