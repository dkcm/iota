/**
 * AroonTest.java  v0.2  22 December 2015 10:11:26 AM
 *
 * Copyright © 2015-2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>Aroon</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.2
 */
public class AroonTest extends AbstractIndicatorTest {

  public AroonTest() {
    super(25);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = AroonTest.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
