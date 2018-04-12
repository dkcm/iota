/**
 * PVITest.java  v0.1  10 April 2018 11:32:07 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>PVI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PVITest extends AbstractIndicatorTest {

  private static final int ONE_YEAR = 255;

  public PVITest() {
    super(ONE_YEAR - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = PVITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
