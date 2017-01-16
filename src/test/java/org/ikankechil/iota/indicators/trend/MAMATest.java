/**
 * MAMATest.java  v0.1  10 January 2017 9:30:27 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MAMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MAMATest extends AbstractIndicatorTest {

  public MAMATest() {
    super(32);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MAMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
