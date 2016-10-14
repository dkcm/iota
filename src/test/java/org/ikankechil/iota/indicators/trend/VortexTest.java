/**
 * VortexTest.java  v0.1  7 October 2016 10:09:25 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Vortex</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VortexTest extends AbstractIndicatorTest {

  public VortexTest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VortexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
