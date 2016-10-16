/**
 * IMITest.java  v0.1  11 February 2016 4:08:47 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>IMI</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class IMITest extends AbstractIndicatorTest {

  public IMITest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = IMITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
