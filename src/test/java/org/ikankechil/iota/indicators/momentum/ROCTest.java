/**
 * ROCTest.java v0.2 3 December 2015 9:20:50 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>ROC</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class ROCTest extends AbstractIndicatorTest {

  public ROCTest() {
    super(10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    TEST_CLASS = ROCTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
