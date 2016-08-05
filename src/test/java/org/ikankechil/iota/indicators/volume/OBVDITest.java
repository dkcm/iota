/**
 * OBVDITest.java  v0.2  21 November 2015 11:07:53 pm
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.volume;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>OBVDI</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class OBVDITest extends AbstractIndicatorTest {

  public OBVDITest() {
    super(32);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = OBVDITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
