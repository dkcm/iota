/**
 * RMFTest.java  v0.2  23 April 2018 11:16:09 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RMF</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RMFTest extends AbstractIndicatorTest {

  public RMFTest() {
    super(15);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RMFTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
