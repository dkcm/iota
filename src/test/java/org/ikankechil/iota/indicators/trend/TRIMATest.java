/**
 * TRIMATest.java  v0.1  17 October 2016 7:06:27 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TRIMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class TRIMATest extends AbstractIndicatorTest {

  public TRIMATest() {
    super(29);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TRIMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
