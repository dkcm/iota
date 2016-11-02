/**
 * KDJTest.java  v0.1  2 November 2016 12:08:48 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>KDJ</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class KDJTest extends AbstractIndicatorTest {

  public KDJTest() {
    super(17);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = KDJTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
