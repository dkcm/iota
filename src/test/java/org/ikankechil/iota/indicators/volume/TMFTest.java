/**
 * TMFTest.java  v0.2 24 November 2015 6:53:35 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>TMF</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class TMFTest extends AbstractIndicatorTest {

  public TMFTest() {
    super(21);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = TMFTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
