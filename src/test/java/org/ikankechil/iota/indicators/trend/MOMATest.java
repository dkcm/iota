/**
 * MOMATest.java  v0.1  9 October 2016 12:06:23 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MOMA</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MOMATest extends AbstractIndicatorTest {

  public MOMATest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MOMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
