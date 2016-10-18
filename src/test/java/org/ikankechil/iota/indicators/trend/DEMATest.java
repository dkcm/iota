/**
 * DEMATest.java  v0.1  17 October 2016 10:00:16 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DEMA</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DEMATest extends AbstractIndicatorTest {

  public DEMATest() {
    super(58);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DEMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
