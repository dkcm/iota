/**
 * CMOTest.java v0.1 26 January 2016 8:27:16 PM
 *
 * Copyright © 2015 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CMO</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CMOTest extends AbstractIndicatorTest {

  public CMOTest() {
    super(14);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CMOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
