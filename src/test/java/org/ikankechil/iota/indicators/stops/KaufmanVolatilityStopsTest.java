/**
 * KaufmanVolatilityStopsTest.java  v0.1  20 October 2016 12:45:00 am
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.stops;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KaufmanVolatilityStops</code>.
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KaufmanVolatilityStopsTest extends AbstractIndicatorTest {

  public KaufmanVolatilityStopsTest() {
    super(21);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KaufmanVolatilityStopsTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
