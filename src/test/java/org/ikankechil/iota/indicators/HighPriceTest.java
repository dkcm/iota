/**
 * HighPriceTest.java	v0.1	11 September 2016 12:09:52 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators;

import java.io.IOException;

import org.junit.BeforeClass;

/**
 * JUnit test for <code>HighPrice</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class HighPriceTest extends AbstractIndicatorTest {

  public HighPriceTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = HighPriceTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
