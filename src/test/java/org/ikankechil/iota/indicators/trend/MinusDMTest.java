/**
 * MinusDMTest.java  v0.1  10 November 2016 1:06:02 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MinusDM</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MinusDMTest extends AbstractIndicatorTest {

  public MinusDMTest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MinusDMTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
