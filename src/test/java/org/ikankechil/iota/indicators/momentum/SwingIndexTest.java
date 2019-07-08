/**
 * SwingIndexTest.java  v0.1  24 June 2019 12:11:59 AM
 *
 * Copyright © 2019-present Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for {@code SwingIndex}.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class SwingIndexTest extends AbstractIndicatorTest {

  public SwingIndexTest() {
    super(1);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = SwingIndexTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
