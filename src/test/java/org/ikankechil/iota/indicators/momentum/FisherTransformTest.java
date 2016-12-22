/**
 * FisherTransformTest.java  v0.3  25 November 2015 4:25:07 PM
 *
 * Copyright © 2015-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>FisherTransform</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.3
 */
public class FisherTransformTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 10;

  public FisherTransformTest() {
    super(DEFAULT_PERIOD - 1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = FisherTransformTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
