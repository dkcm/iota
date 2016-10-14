/**
 * VOMOMATest.java  v0.1  9 October 2016 2:39:11 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VOMOMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VOMOMATest extends AbstractIndicatorTest {

  public VOMOMATest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VOMOMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
