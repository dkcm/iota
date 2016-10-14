/**
 * TDITest.java  v0.2  9 December 2015 12:44:46 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TDI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TDITest extends AbstractIndicatorTest {

  public TDITest() {
    super(59);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TDITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
