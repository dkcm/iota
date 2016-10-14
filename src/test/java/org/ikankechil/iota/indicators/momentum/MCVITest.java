/**
 * MCVITest.java	v0.1	15 September 2016 10:50:34 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MCVI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MCVITest extends AbstractIndicatorTest {

  public MCVITest() {
    super(10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MCVITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
