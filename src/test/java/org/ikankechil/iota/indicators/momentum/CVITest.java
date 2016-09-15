/**
 * CVITest.java  v0.1  15 September 2016 2:37:32 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>CVI</code>.
 * <p>
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class CVITest extends AbstractIndicatorTest {

  public CVITest() {
    super(10);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = CVITest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
