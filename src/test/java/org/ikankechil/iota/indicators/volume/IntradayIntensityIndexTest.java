/**
 * IntradayIntensityIndexTest.java  v0.1  10 April 2018 11:54:05 PM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>IntradayIntensityIndex</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IntradayIntensityIndexTest extends AbstractIndicatorTest {

  public IntradayIntensityIndexTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = IntradayIntensityIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
