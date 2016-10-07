/**
 * ChandelierExitTest.java  v0.1  7 October 2016 12:08:19 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ChandelierExit</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ChandelierExitTest extends AbstractIndicatorTest {

  public ChandelierExitTest() {
    super(22);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ChandelierExitTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
