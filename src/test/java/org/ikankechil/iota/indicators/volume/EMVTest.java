/**
 * EMVTest.java  v0.1  15 December 2016 12:59:49 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>EMV</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class EMVTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 14;

  public EMVTest() {
    super(DEFAULT_PERIOD);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = EMVTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
