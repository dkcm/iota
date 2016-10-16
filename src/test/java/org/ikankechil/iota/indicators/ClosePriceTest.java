/**
 * ClosePriceTest.java  v0.1  11 September 2016 12:02:06 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>ClosePrice</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ClosePriceTest extends AbstractIndicatorTest {

  public ClosePriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ClosePriceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
