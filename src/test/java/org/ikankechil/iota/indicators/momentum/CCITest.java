/**
 * CCITest.java  v0.2 25 November 2015 1:45:29 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CCI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class CCITest extends AbstractIndicatorTest {

  public CCITest() {
    super(19);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CCITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
