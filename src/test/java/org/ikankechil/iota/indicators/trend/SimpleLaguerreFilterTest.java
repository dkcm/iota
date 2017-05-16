/**
 * SimpleLaguerreFilterTest.java  v0.1  13 May 2017 11:16:45 pm
 *
 * Copyright © 2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 *
 *
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SimpleLaguerreFilterTest extends AbstractIndicatorTest {

  public SimpleLaguerreFilterTest() {
    super(4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SimpleLaguerreFilterTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
