/**
 * MassIndexTest.java  v0.1  14 December 2016 11:04:46 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>MassIndex</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class MassIndexTest extends AbstractIndicatorTest {

  private static final int DEFAULT_EMA = 9;
  private static final int DEFAULT_SUM = 25;

  public MassIndexTest() {
    super((DEFAULT_EMA - 1) * 2 + DEFAULT_SUM - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MassIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
