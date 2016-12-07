/**
 * DPOTest.java  v0.1  7 December 2016 9:45:13 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DPO</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class DPOTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 20;

  public DPOTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DPOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
