/**
 * PZOTest.java  v0.2  11 December 2015 12:55:27 am
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>PZO</code>.
 * <p>
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class PZOTest extends AbstractIndicatorTest {

  public PZOTest() {
    super(13);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = PZOTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
