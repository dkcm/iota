/**
 * CamarillaPivotPointsTest.java  v0.1  28 May 2018 1:28:34 AM
 *
 * Copyright © 2018 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CamarillaPivotPoints</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CamarillaPivotPointsTest extends AbstractIndicatorTest {

  public CamarillaPivotPointsTest() {
    super(0);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CamarillaPivotPointsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
