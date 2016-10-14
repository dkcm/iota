/**
 * VHFTest.java  v0.1  7 October 2016 12:58:35 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>VHF</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class VHFTest extends AbstractIndicatorTest {

  public VHFTest() {
    super(28);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = VHFTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
