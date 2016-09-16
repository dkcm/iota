/**
 * NATRTest.java  v0.1  17 September 2016 1:18:18 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volatility;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>NATR</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class NATRTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public NATRTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = NATRTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
