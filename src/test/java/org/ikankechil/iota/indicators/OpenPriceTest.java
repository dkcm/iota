/**
 * OpenPriceTest.java  v0.1  11 September 2016 12:09:14 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>OpenPrice</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class OpenPriceTest extends AbstractIndicatorTest {

  public OpenPriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = OpenPriceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
