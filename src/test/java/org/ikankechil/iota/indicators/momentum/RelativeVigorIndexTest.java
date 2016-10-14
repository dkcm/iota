/**
 * RelativeVigorIndexTest.java  v0.2 15 July 2015 1:05:43 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>RelativeVigorIndex</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class RelativeVigorIndexTest extends AbstractIndicatorTest {

  private static final int PERIOD = 10;

  public RelativeVigorIndexTest() {
    super(PERIOD + 2);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = RelativeVigorIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
