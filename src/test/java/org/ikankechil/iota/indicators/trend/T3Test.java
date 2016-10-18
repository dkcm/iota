/**
 * T3Test.java  v0.1  17 October 2016 7:30:39 pm
 *
 * Copyright © 2016 Daniel Kuan.  All rights reserved.
 */
package org.ikankechil.iota.indicators.trend;

import java.io.IOException;

import org.ikankechil.iota.indicators.AbstractIndicatorTest;
import org.junit.BeforeClass;

/**
 * JUnit test for <code>T3</code>.
 *
 *
 * @author Daniel Kuan
 * @version 0.1
 */
public class T3Test extends AbstractIndicatorTest {

  public T3Test() {
    super(24);
  }

  @BeforeClass
  public static void setUpBeforeClass() throws IOException {
    TEST_CLASS = T3Test.class;
    AbstractIndicatorTest.setUpBeforeClass();
  }

}
