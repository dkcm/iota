/**
 * FisherTransformTest.java v0.2 25 November 2015 4:25:07 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>FisherTransform</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class FisherTransformTest extends AbstractIndicatorTest {

  public FisherTransformTest() {
    super(15);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = FisherTransformTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
