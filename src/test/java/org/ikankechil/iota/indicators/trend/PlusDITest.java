/**
 * PlusDITest.java  v0.1  10 November 2016 1:05:51 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>PlusDI</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PlusDITest extends AbstractIndicatorTest {

  public PlusDITest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = PlusDITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
