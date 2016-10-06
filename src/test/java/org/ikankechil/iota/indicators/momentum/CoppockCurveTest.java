/**
 * CoppockCurveTest.java  v0.1  6 October 2016 9:46:03 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CoppockCurve</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CoppockCurveTest extends AbstractIndicatorTest {

  public CoppockCurveTest() {
    super(23);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CoppockCurveTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
