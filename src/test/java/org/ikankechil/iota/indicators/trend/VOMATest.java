/**
 * VOMATest.java  v0.1  9 October 2016 2:28:51 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VOMA</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VOMATest extends AbstractIndicatorTest {

  public VOMATest() {
    super(3);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VOMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
