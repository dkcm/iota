/**
 * ALMATest.java  v0.1  11 October 2016 12:11:25 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ALMA</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class ALMATest extends AbstractIndicatorTest {

  public ALMATest() {
    super(8);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = ALMATest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
