/**
 * PVTTest.java  0.1  19 December 2016 1:50:50 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JUnit test for <code>PVT</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class PVTTest extends AbstractIndicatorTest {

  public PVTTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = PVTTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
