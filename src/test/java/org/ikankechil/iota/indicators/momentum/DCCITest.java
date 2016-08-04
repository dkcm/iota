/**
 * DCCITest.java v0.2 25 November 2015 1:52:24 PM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>DCCI</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class DCCITest extends AbstractIndicatorTest {

  public DCCITest() {
    super(19 + 4);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = DCCITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
