/**
 * ZigZagTest.java  v0.1  21 January 2016 9:25:24 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 *
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ZigZagTest extends AbstractIndicatorTest {

  public ZigZagTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ZigZagTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
