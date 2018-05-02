/**
 * RMOTest.java  v0.1  23 April 2018 11:16:40 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RMO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class RMOTest extends AbstractIndicatorTest {

  public RMOTest() {
    super(44);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RMOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
