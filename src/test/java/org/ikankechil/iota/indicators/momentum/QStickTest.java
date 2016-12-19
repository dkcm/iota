/**
 * QStickTest.java  0.1  19 December 2016 6:58:39 PM
 *
 * Copyright © 2016-2017 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.momentum;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>QStick</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class QStickTest extends AbstractIndicatorTest {

  private static final int DEFAULT_PERIOD = 12;
  private static final int DEFAULT_SIGNAL = 9;

  public QStickTest() {
    super((DEFAULT_PERIOD - 1) + (DEFAULT_SIGNAL - 1));
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = QStickTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
