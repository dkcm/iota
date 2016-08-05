/**
 * MomentumTest.java  v0.2 3 December 2015 11:05:13 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Momentum</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class MomentumTest extends AbstractIndicatorTest {

  public MomentumTest() {
    super(10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = MomentumTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
