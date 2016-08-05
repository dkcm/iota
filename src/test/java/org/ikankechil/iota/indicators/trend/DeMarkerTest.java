/**
 * DeMarkerTest.java  v0.2  22 November 2015 11:05:26 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DeMarker</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DeMarkerTest extends AbstractIndicatorTest {

  public DeMarkerTest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DeMarkerTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
