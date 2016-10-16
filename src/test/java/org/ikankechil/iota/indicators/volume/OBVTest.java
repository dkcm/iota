/**
 * OBVTest.java  v0.2  7 December 2015 6:56:21 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 * JUnit test for <code>OBV</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OBVTest extends AbstractIndicatorTest {

  public OBVTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = OBVTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

  @Ignore@Override
  public void cannotInstantiateWithNegativePeriod() {
    // not supported
  }

}
