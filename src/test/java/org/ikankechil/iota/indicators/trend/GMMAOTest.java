/**
 * GMMAOTest.java  v0.1  16 April 2018 11:01:28 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>GMMAO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class GMMAOTest extends AbstractIndicatorTest {

  public GMMAOTest() {
    super(59 + 12);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = GMMAOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
